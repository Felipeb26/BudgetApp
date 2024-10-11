package com.batsworks.budget

import android.Manifest
import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.batsworks.budget.components.capitalizeStrings
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.navigation.Navigate
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.formatNavigation
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.ui.theme.BudgetTheme
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.CustomTheme
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.findTheme
import com.batsworks.budget.ui.view_model.login.BiometricPromptManager
import com.batsworks.budget.ui.view_model.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.time.delay
import java.time.Duration

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private val model by viewModels<MainViewModel>()
	private val promptManager by lazy { BiometricPromptManager(this) }
	private val permissionsToRequest = mutableListOf(Manifest.permission.CAMERA)
	private val defaultRgbColor = Color800.toArgb()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		installSplashScreen().apply { setKeepOnScreenCondition { !model.isReady.value } }

		enableEdgeToEdge(
			SystemBarStyle.auto(defaultRgbColor, defaultRgbColor),
			SystemBarStyle.auto(defaultRgbColor, defaultRgbColor)
		)

		val context = applicationContext
		val notificationToast = NotificationToast(context)

		setContent {
			val view = LocalView.current
			CustomTheme(view)
			HandleBiometricPrompt(context, notificationToast)
			HandlePermissionsRequest()

			var imageUri by remember { mutableStateOf<Uri?>(null) }
			val userState by model.userEntity.collectAsState()

			BudgetTheme {
				HandleExtrasRequests(permissionsToRequest)
				CustomTheme(view, findTheme(userState?.theme))

				imageUri?.let {
					val encodedUri = Uri.encode(it.toString())
					Navigate(screen = Screen.SharedReceiptScreen.withArgs(encodedUri))
					return@BudgetTheme
				}

				HandleIntent(intent) { imageUri = it }

				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(customBackground)
				) {}

				if (userState?.loginWhenEnter == false) {
					promptManager.showBiometricPrompt(
						capitalizeStrings(
							"${stringResource(id = R.string.enterprise_name)} ${stringResource(id = R.string.app_name)}"
						),
						stringResource(id = R.string.biometric_description)
					)
				} else {
					SelectScreen(userState)
				}
			}
		}
	}

	@Composable
	private fun HandleBiometricPrompt(context: Context, notificationToast: NotificationToast) {
		var whereToGo by remember { mutableStateOf<String?>(null) }

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
			if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationSucess) {
				whereToGo = Screen.MainScreen.route
			}
			if(biometricResult is BiometricPromptManager.BiometricResult.AuthenticationErro){
				Log.d("biometria", (biometricResult as BiometricPromptManager.BiometricResult.AuthenticationErro).error)
				notificationToast.show((biometricResult as BiometricPromptManager.BiometricResult.AuthenticationErro).error)
				whereToGo = Screen.LoginScreen.route
			}
			if(biometricResult is BiometricPromptManager.BiometricResult.AuthenticationFailed){
				Log.d("biometria", context.getString(R.string.biometric_auth_error))
				notificationToast.show(context.getString(R.string.biometric_auth_error))
				whereToGo = Screen.LoginScreen.route
			}
		}
		whereToGo?.let { Navigate(screen = formatNavigation(it)) }
	}

	@Composable
	private fun HandlePermissionsRequest() {
		val permissionsResultLauncher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.RequestMultiplePermissions(),
			onResult = { })

		LaunchedEffect(Unit) {
			delay(Duration.ofSeconds(2))
			permissionsResultLauncher.launch(permissionsToRequest.toTypedArray())
		}
	}

	@Composable
	private fun HandleIntent(intent: Intent, onImageUriReceived: (Uri?) -> Unit) {
		LaunchedEffect(intent) {
			if (intent.action == Intent.ACTION_SEND && intent.type != null) {
				val type = intent.type
				if (type?.startsWith("image") == true) {
					onImageUriReceived(intent.clipData?.getItemAt(0)?.uri ?: intent.data)
				}
			}
		}
	}

	@Composable
	private fun HandleExtrasRequests(permissionsToRequest: MutableList<String>) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
			permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
			permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			permissionsToRequest.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
			permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
		}
	}

	@Composable
	private fun SelectScreen(user: UserEntity?) {
		if (user?.loginWhenEnter == true) {
			Navigate(screen = formatNavigation(Screen.MainScreen.route))
		} else {
			Navigate(screen = formatNavigation(Screen.LoginScreen.route))
		}
	}
}
