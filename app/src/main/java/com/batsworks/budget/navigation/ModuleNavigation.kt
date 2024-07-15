package com.batsworks.budget.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.batsworks.budget.domain.dao.Collection
import com.batsworks.budget.domain.entity.UserFirebaseEntity
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.add.AddViewModel
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.login.LoginViewModel
import com.batsworks.budget.ui.view_model.login.SignInViewModel
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.SharedReceipt
import com.batsworks.budget.ui.views.SignUp

@Composable
fun ModuleNavigation(
	navController: NavHostController = rememberNavController(),
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
							UserFirebaseEntity::class.java
						)
					)
				)
			)
			SignUp(navController, model)
		}

		composable(
			Screen.SharedReceiptScreen.route + "/{uri}",
			arguments = listOf(navArgument("uri") { type = NavType.StringType })
		) { backStackEntry ->
			val model = viewModel<AddViewModel>(
				factory = factoryProvider(
					AddViewModel(
						repository = CustomRepository(
							Collection.AMOUNTS.path,
							AmountEntity::class.java
						)
					)
				)
			)
			val uri = Uri.parse(backStackEntry.arguments?.getString("uri"))
			SharedReceipt(navController, uri, model.resourceEventFlow, model.state, model::onEvent)
		}
		navigation(Screen.MainScreen.route, route = "main") {
			composable(Screen.MainScreen.route) { Main() }
		}
	}
}

private fun goTo(route: String): String {
	return route.lowercase().substring(0, route.indexOf("_"))
}