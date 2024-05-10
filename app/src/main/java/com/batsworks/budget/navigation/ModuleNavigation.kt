package com.batsworks.budget.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.ui.state.factoryProvider
import com.batsworks.budget.ui.state.login.LoginViewModel
import com.batsworks.budget.ui.state.login.SignInViewModel
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.SignUp

@Composable
fun StartNavigate(
	navController: NavHostController,
	screen: Screen,
	route: Boolean = false,
) {
	NavHost(
		navController = navController,
		startDestination = if (route) goTo(screen.route) else screen.route,
		modifier = Modifier.background(customBackground)
	) {
		composable(Screen.LoginScreen.route) {
			val model = viewModel<SignInViewModel>(
				factory = factoryProvider(
					SignInViewModel(
						CustomRepository(
							"users",
							UserEntity::class.java
						)
					)
				)
			)
			Login(navController, model)
		}
		composable(Screen.SignUpScreen.route) {
			val model = viewModel<LoginViewModel>(
				factory = factoryProvider(
					LoginViewModel(
						repository = CustomRepository(
							"users",
							UserEntity::class.java
						)
					)
				)
			)
			SignUp(navController, model)
		}
		navigation(Screen.MainScreen.route, route = "main") {
			composable(Screen.MainScreen.route) { Main() }
		}
	}
}

private fun goTo(route: String): String {
	return route.lowercase().substring(0, route.indexOf("_"))
}