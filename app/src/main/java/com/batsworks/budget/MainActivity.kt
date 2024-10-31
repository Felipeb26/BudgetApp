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
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.batsworks.budget.navigation.FileType
import com.batsworks.budget.navigation.MainNavigate
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.services.biometric.BiometricPromptManager
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.services.worker.SyncData
import com.batsworks.budget.ui.components.menu.AJUST_TAG
import com.batsworks.budget.ui.components.texts.capitalizeStrings
import com.batsworks.budget.ui.theme.BudgetTheme
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.custom.CustomTheme
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.time.delay
import java.time.Duration

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private val mainViewModel by viewModels<MainViewModel>()
	private val promptManager by lazy { BiometricPromptManager(this) }
	private val permissionsToRequest = mutableListOf(Manifest.permission.CAMERA)
	private val defaultRgbColor = Color800.toArgb()
	private val tag = MainActivity::class.java.name

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen().apply { setKeepOnScreenCondition { !mainViewModel.isReady.value } }

		enableEdgeToEdge(
			SystemBarStyle.auto(defaultRgbColor, defaultRgbColor),
			SystemBarStyle.auto(defaultRgbColor, defaultRgbColor)
		)

		val context = applicationContext
		val notificationToast = NotificationToast(context)

		setContent {
			val	customTheme = CustomTheme(LocalView.current)

			val (whereToGo, setWhereToGo) = remember { mutableStateOf<Screen?>(null) }
			HandlePermissionsRequest()
			HandleBiometricPrompt(context, notificationToast, setWhereToGo)

			val (sharedFile, setSharedFileInfo) = remember {
				mutableStateOf<Pair<Uri?, FileType>?>(
					null
				)
			}
			val user by mainViewModel.userStateFlow.collectAsState()
			BudgetTheme {
				HandleExtrasRequests(permissionsToRequest)
				mainViewModel.changeTheme(customTheme)

				sharedFile?.let {
					val encodedUri = Uri.encode(it.first.toString())
					val shared = Screen.SharedReceiptScreen(fileUri = encodedUri, fileType = it.second)
					MainNavigate(screen = shared)
					return@BudgetTheme
				}

				whereToGo?.let { to ->
					MainNavigate(screen = to)
					return@BudgetTheme
				}

				HandleIntent(intent, setSharedFileInfo)

				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(customBackground)
				) {}

				if (user == null) {
					MainNavigate(screen = Screen.LoginScreen)
					return@BudgetTheme
				} else if (user?.loginWhenEnter == true) {
					syncData()
					MainNavigate(screen = Screen.MainScreen)
					return@BudgetTheme
				} else if (user?.loginWhenEnter == false) {
					promptManager.showBiometricPrompt(
						capitalizeStrings(
							"${stringResource(id = R.string.enterprise_name)} ${
								stringResource(id = R.string.app_name)}"
						), stringResource(id = R.string.biometric_description)
					)
				}
			}
		}
	}

	@Composable
	private fun HandleBiometricPrompt(
		context: Context,
		notificationToast: NotificationToast,
		whereToGo: (Screen) -> Unit,
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
				} else whereToGo.invoke(Screen.LoginScreen)
			}
		}

		biometricResult?.let { result ->
			when (result) {
				is BiometricPromptManager.BiometricResult.AuthenticationSucess -> whereToGo.invoke(Screen.MainScreen)

				is BiometricPromptManager.BiometricResult.AuthenticationErro -> {
					notificationToast.show((biometricResult as BiometricPromptManager.BiometricResult.AuthenticationErro).error)
					whereToGo.invoke(Screen.LoginScreen)
				}

				is BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
					Log.d("biometria", context.getString(R.string.biometric_auth_error))
					notificationToast.show(context.getString(R.string.biometric_auth_error))
					whereToGo.invoke(Screen.LoginScreen)
				}

				else -> whereToGo.invoke(Screen.LoginScreen)
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
	private fun HandleIntent(intent: Intent, onImageUriReceived: (Pair<Uri?, FileType>) -> Unit) {
		LaunchedEffect(intent) {
			if (intent.action == Intent.ACTION_SEND && intent.type != null) {
				val type = intent.type
				if (type?.startsWith("image") == true) {
					onImageUriReceived(
						Pair(
							intent.clipData?.getItemAt(0)?.uri ?: intent.data,
							FileType.IMG
						)
					)
				} else if (type?.endsWith("pdf") == true) {
					onImageUriReceived(
						Pair(
							intent.clipData?.getItemAt(0)?.uri ?: intent.data,
							FileType.PDF
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
			.setInitialDelay(Duration.ofMinutes(15))
			.addTag(tag)
			.build()

		val workManager = WorkManager.getInstance(this)
		workManager.cancelAllWork()

		workManager.getWorkInfosForUniqueWorkLiveData(AJUST_TAG(tag))
			.observeForever { workInfos ->
				workInfos.forEach { Log.d("DATA_SYNC_STATE", "${it.state}") }

				val isWorkEnqueued = workInfos.any { workInfo ->
					workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING || workInfo.state == WorkInfo.State.BLOCKED
				}
				if(!isWorkEnqueued){
					workManager.enqueueUniquePeriodicWork(AJUST_TAG(tag), ExistingPeriodicWorkPolicy.KEEP, workerRequest)
					Log.d("DATA_SYNC_STATE", "Novo Worker agendado.")
				} else {
					Log.d("WORK_SCHEDULE", "Worker já está agendado ou em execução.")
				}
			}
	}

}
