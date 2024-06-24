package com.batsworks.budget

import android.Manifest
import android.content.Intent
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.capitalizeStrings
import com.batsworks.budget.components.notification.NotificationToast
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.StartNavigate
import com.batsworks.budget.ui.theme.BudgetTheme
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.CustomTheme
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.findTheme
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
			val view = LocalView.current
			CustomTheme(LocalView.current)
			val biometricResult by promptManager.promptResults.collectAsState(initial = null)
			val enrollLauncher = rememberLauncherForActivityResult(
				contract = ActivityResultContracts.StartActivityForResult(),
				onResult = { Log.d("LAUNCHER", "Activity $it") })

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
				val context = LocalContext.current
				val userState = profile.userEntity.collectAsState()
				val user = rememberSaveable { mutableStateOf(userState.value?.nome) }
				val toast = NotificationToast(context)

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
					permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
					permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
					permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
				}

				CustomTheme(view, findTheme(userState.value?.theme))
				biometricResult?.let { result ->
					when (result) {
						is BiometricPromptManager.BiometricResult.AuthenticationSucess -> {
							StartNavigate(navController, Screen.MainScreen, true)
							return@BudgetTheme
						}

						is BiometricPromptManager.BiometricResult.AuthenticationErro -> {
							Log.d("biometria", result.error)
							toast.show(result.error)
							StartNavigate(navController, Screen.LoginScreen)
							return@BudgetTheme
						}

						BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
							Log.d("biometria", context.getString(R.string.biometric_auth_error))
							toast.show(context.getString(R.string.biometric_auth_error))
							StartNavigate(navController, Screen.LoginScreen)
							return@BudgetTheme
						}

						else -> Unit
					}
				}

				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(customBackground)
				) {}
				if (user.value != null) {
					promptManager.showBiometricPrompt(
						capitalizeStrings(
							"${stringResource(id = R.string.enterprise_name)} ${stringResource(id = R.string.app_name)}"
						),
						stringResource(id = R.string.biometric_description)
					)
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