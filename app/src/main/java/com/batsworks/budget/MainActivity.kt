package com.batsworks.budget

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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


class MainActivity : AppCompatActivity() {

	private val model by viewModels<MainViewModel>()
	private val promptManager by lazy { BiometricPromptManager(this) }
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

			BudgetTheme {
				val model = viewModel<ProfileViewModel>()
				val userState = model.userEntity.collectAsState()
				val navController = rememberNavController()
				rollbar = Rollbar.init(LocalContext.current)
				rollbar?.debug("Here is some debug message");

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

				Column(
					Modifier
						.fillMaxSize()
						.background(customBackground)
				) {}
				SelectScreen(userState.value, navController, promptManager)
			}
		}
	}
}

@Composable
private fun SelectScreen(
	userEntity: UserEntity?,
	navController: NavHostController,
	promptManager: BiometricPromptManager,
) {
	if (userEntity == null) {
		StartNavigate(navController, Screen.LoginScreen)
		return
	}

	userEntity.let { user ->
		if (user.loginWhenEnter) {
			StartNavigate(navController, Screen.LoginScreen, true)

		} else {
			promptManager.showBiometricPrompt(
				"teste biometria",
				"testar a biometria para login"
			)
		}
	}
}