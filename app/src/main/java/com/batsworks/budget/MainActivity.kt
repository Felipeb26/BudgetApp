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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.batsworks.budget.components.AJUST_TAG
import com.batsworks.budget.components.texts.capitalizeStrings
import com.batsworks.budget.navigation.Navigate
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.formatNavigation
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.services.worker.SyncData
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
	private val tag = MainActivity::class.java.name

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
			val (whereToGo, setWhereToGo) = remember { mutableStateOf<String?>(null) }
			CustomTheme(LocalView.current)
			HandlePermissionsRequest()
			HandleBiometricPrompt(context, notificationToast, setWhereToGo)

			val (sharedFile, setSharedFileInfo) = remember { mutableStateOf<Pair<Uri?, String>?>(null) }
			val user by model.userEntity.collectAsState()

			BudgetTheme {
				HandleExtrasRequests(permissionsToRequest)
				CustomTheme(LocalView.current, findTheme(user?.theme))

				sharedFile?.let {
					val encodedUri = Uri.encode(it.first.toString())
					Navigate(screen = Screen.SharedReceiptScreen.withArgs(encodedUri, it.second))
					return@BudgetTheme
				}

				whereToGo?.let { to ->
					Navigate(screen = to)
					return@BudgetTheme
				}

				HandleIntent(intent, setSharedFileInfo)

				Column(
					modifier = Modifier
                        .fillMaxSize()
                        .background(customBackground)
				) {}

				if (user == null) {
					Navigate(screen = formatNavigation(Screen.LoginScreen.route))
					return@BudgetTheme
				} else if (user?.loginWhenEnter == true) {
                    syncData()
					Navigate(screen = formatNavigation(Screen.MainScreen.route))
                    return@BudgetTheme
				} else if (user?.loginWhenEnter == false) {
					promptManager.showBiometricPrompt(
						capitalizeStrings("${stringResource(id = R.string.enterprise_name)} ${stringResource(id = R.string.app_name)}"),
						stringResource(id = R.string.biometric_description)
					)
				}
			}
		}
	}

	@Composable
	private fun HandleBiometricPrompt(
        context: Context,
        notificationToast: NotificationToast,
        whereToGo: (String) -> Unit,
    ) {
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
				} else whereToGo.invoke(formatNavigation(Screen.LoginScreen.route))
			}
		}

		biometricResult?.let { result ->
			when (result) {
				is BiometricPromptManager.BiometricResult.AuthenticationSucess -> whereToGo.invoke(
					formatNavigation(Screen.MainScreen.route)
				)

				is BiometricPromptManager.BiometricResult.AuthenticationErro -> {
					notificationToast.show((biometricResult as BiometricPromptManager.BiometricResult.AuthenticationErro).error)
					whereToGo.invoke(formatNavigation(Screen.LoginScreen.route))
				}

				is BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
					Log.d("biometria", context.getString(R.string.biometric_auth_error))
					notificationToast.show(context.getString(R.string.biometric_auth_error))
					whereToGo.invoke(formatNavigation(Screen.LoginScreen.route))
				}

				else -> whereToGo.invoke(formatNavigation(Screen.LoginScreen.route))
			}
		}
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
	private fun HandleIntent(intent: Intent, onImageUriReceived: (Pair<Uri?, String>) -> Unit) {
		LaunchedEffect(intent) {
			if (intent.action == Intent.ACTION_SEND && intent.type != null) {
				val type = intent.type
				if (type?.startsWith("image") == true) {
					onImageUriReceived(
						Pair(
							intent.clipData?.getItemAt(0)?.uri ?: intent.data,
							"IMG"
						)
					)
				} else if (type?.endsWith("pdf") == true) {
					onImageUriReceived(
						Pair(
							intent.clipData?.getItemAt(0)?.uri ?: intent.data,
							"PDF"
						)
					)
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


	private fun syncData() {
		val contraints = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.build()

		val workerRequest = PeriodicWorkRequest.Builder(SyncData::class.java, Duration.ofHours(6))
			.setConstraints(contraints)
			.setInitialDelay(Duration.ofSeconds(15))
			.addTag(tag)
			.build()

		val workManager = WorkManager.getInstance(this)
		workManager.cancelAllWork()

		workManager.enqueueUniquePeriodicWork(
			AJUST_TAG(tag),
			ExistingPeriodicWorkPolicy.KEEP,
			workerRequest
		)
		workManager.getWorkInfosForUniqueWorkLiveData(AJUST_TAG(tag))
			.observeForever {
				it.forEach { workInfo ->
					Log.d("DATA_SYNC_STATE", "${workInfo.state}")
				}
			}

	}

}
