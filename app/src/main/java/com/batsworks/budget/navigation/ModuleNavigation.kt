package com.batsworks.budget.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.batsworks.budget.domain.dao.Collection
import com.batsworks.budget.domain.dto.UserDTO
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.login.LoginViewModel
import com.batsworks.budget.ui.view_model.login.SignInViewModel
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.SharedReceipt
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
			val model = viewModel<SignInViewModel>()
			Login(navController, model.state, model.validationEvents, model::onEvent)
		}
		composable(Screen.SignUpScreen.route) {
			val model = viewModel<LoginViewModel>(
				factory = factoryProvider(
					LoginViewModel(
						repository = CustomRepository(
							Collection.USERS.path,
							UserDTO::class.java
						)
					)
				)
			)
			SignUp(navController, model)
		}
		navigation(Screen.MainScreen.route, route = "main") {
			composable(Screen.MainScreen.route) { Main() }
		}

		composable(
			Screen.SharedReceiptScreen.route + "/{file}",
			arguments = listOf(navArgument("file") {
				type = NavType.StringType
				defaultValue = null
				nullable = true
			})
		) { entry ->
			SharedReceipt(entry.arguments?.getString("file")?: "")
		}
	}
}

private fun goTo(route: String): String {
	return route.lowercase().substring(0, route.indexOf("_"))
}