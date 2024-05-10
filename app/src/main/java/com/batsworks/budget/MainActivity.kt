package com.batsworks.budget

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.StartNavigate
import com.batsworks.budget.ui.state.profile.ProfileViewModel
import com.batsworks.budget.ui.theme.BudgetTheme
import com.batsworks.budget.ui.theme.Color800

class MainActivity : ComponentActivity() {

	private val model by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen().apply {
			setKeepOnScreenCondition { !model.isReady.value }

			setOnExitAnimationListener { screen ->
				val zoomX = ObjectAnimator.ofFloat(screen.iconView, View.SCALE_X, 0.4f, 0.0f)
				zoomX.interpolator = OvershootInterpolator()
				zoomX.duration = 500L
				zoomX.doOnEnd { screen.remove() }

				val zoomY = ObjectAnimator.ofFloat(screen.iconView, View.SCALE_Y, 0.4f, 0.0f)
				zoomY.interpolator = OvershootInterpolator()
				zoomY.duration = 500L
				zoomY.doOnEnd { screen.remove() }
				zoomY.start()
				zoomX.start()
			}
		}
		enableEdgeToEdge(
			statusBarStyle = SystemBarStyle.auto(Color800.toArgb(), Color800.toArgb()),
			navigationBarStyle = SystemBarStyle.auto(Color800.toArgb(), Color800.toArgb())
		)
		setContent {
			BudgetTheme {
				val model = viewModel<ProfileViewModel>()
				val userState = model.userEntity.collectAsState()

				if (userState.value == null) {
					StartNavigate(
						navController = rememberNavController(),
						screen = Screen.LoginScreen
					)
				} else {
					StartNavigate(
						navController = rememberNavController(),
						screen = Screen.MainScreen,
						true
					)
				}
			}
		}
	}
}