package com.batsworks.budget

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.StartNavigate
import com.batsworks.budget.ui.theme.BudgetTheme
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.login.BiometricPromptManager
import com.batsworks.budget.ui.view_model.profile.ProfileViewModel
import com.rollbar.android.Rollbar
import kotlinx.coroutines.time.delay
import java.time.Duration


class MainActivity : AppCompatActivity() {

    private val promptManager by lazy { BiometricPromptManager(this) }
    private val model by viewModels<MainViewModel>()
    private val profile by viewModels<ProfileViewModel>()
    private val permissionsToRequest = mutableListOf(Manifest.permission.CAMERA)
    private var rollbar: Rollbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply { setKeepOnScreenCondition { !model.isReady.value } }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color800.toArgb(), Color800.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(Color800.toArgb(), Color800.toArgb())
        )

        setContent {
            val biometricResult by promptManager.promptResults.collectAsState(initial = null)
            val enrollLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { println("Activity $it") })

            LaunchedEffect(biometricResult) {
                if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
                    if (Build.VERSION.SDK_INT >= 30) {
                        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                        enrollLauncher.launch(enrollIntent)
                    }
                }
            }
            val permissionsResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { })


            LaunchedEffect(Unit) {
                delay(Duration.ofSeconds(2))
                permissionsResultLauncher.launch(permissionsToRequest.toTypedArray())
            }

            BudgetTheme {
                rollbar = Rollbar.init(LocalContext.current)
                val navController = rememberNavController()
                val userState = profile.userEntity.collectAsState()
                val user = rememberSaveable { mutableStateOf(userState.value?.nome) }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
                }

                biometricResult?.let { result ->
                    when (result) {
                        is BiometricPromptManager.BiometricResult.AuthenticationSucess -> {
                            StartNavigate(navController, Screen.MainScreen, true)
                            return@BudgetTheme
                        }

                        is BiometricPromptManager.BiometricResult.AuthenticationErro -> {
                            Log.d("biometria", result.error)
                            StartNavigate(navController, Screen.LoginScreen)
                            return@BudgetTheme
                        }

                        BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                            Log.d("biometria", "houuve falha ao se autenticar")
                            StartNavigate(navController, Screen.LoginScreen)
                            return@BudgetTheme
                        }

                        else -> Unit
                    }
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(customBackground)) {}
                if (user.value != null) {
                    promptManager.showBiometricPrompt("BatsWorks Budget", "")
                } else SelectScreen(userState.value)
            }
        }
    }
}

@Composable
private fun SelectScreen(user: UserEntity?) {
    if (user?.loginWhenEnter == true) {
        StartNavigate(
            navController = rememberNavController(),
            screen = Screen.MainScreen, true
        )
    } else {
        StartNavigate(
            navController = rememberNavController(),
            screen = Screen.LoginScreen
        )
    }
}

private fun Activity.openAppSetting() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

private fun checkPermissionFor(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

private fun formatPermissionName(permission: String): String {
    return if (permission.lastIndexOf("_") > 0) {
        permission.substring(permission.lastIndexOf("_"))
    } else if (permission.lastIndexOf(".") > 0) {
        permission.substring(permission.lastIndexOf("."))
    } else {
        permission
    }
}