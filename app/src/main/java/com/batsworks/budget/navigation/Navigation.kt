package com.batsworks.budget.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.batsworks.budget.ui.view_model.add.AddViewModel
import com.batsworks.budget.ui.view_model.history.HistoryViewModel
import com.batsworks.budget.ui.view_model.home.HomeViewModel
import com.batsworks.budget.ui.view_model.login.LoginViewModel
import com.batsworks.budget.ui.view_model.login.SignInViewModel
import com.batsworks.budget.ui.view_model.profile.ProfileViewModel
import com.batsworks.budget.ui.view_model.receipt.ReceiptViewModel
import com.batsworks.budget.ui.view_model.settings.SettingsViewModel
import com.batsworks.budget.ui.views.Accounts
import com.batsworks.budget.ui.views.Add
import com.batsworks.budget.ui.views.Historico
import com.batsworks.budget.ui.views.Home
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.PlusScreen
import com.batsworks.budget.ui.views.Profile
import com.batsworks.budget.ui.views.ReceiptScreen
import com.batsworks.budget.ui.views.Setting
import com.batsworks.budget.ui.views.SharedReceipt
import com.batsworks.budget.ui.views.SignUp

@Composable
fun Navigate(
	navController: NavHostController = rememberNavController(),
	screen: String,
	paddingValues: PaddingValues? = null,
	route: String? = null,
) {
	NavHost(
		navController = navController,
		route = route,
		startDestination = screen,
		modifier = if (paddingValues != null) Modifier.padding(paddingValues) else Modifier
	) {

		navigation(Screen.LoginScreen.route, route = "login_graph") {
			composable(Screen.LoginScreen.route) {
				val model = hiltViewModel<SignInViewModel>()
				Login(navController, model.state, model.validationEvents, model::onEvent)
			}

			composable(Screen.SignUpScreen.route) {
				val model = hiltViewModel<LoginViewModel>()
				SignUp(navController, model)
			}
		}

		navigation(Screen.MainScreen.route, route = "main_graph") {
			composable(Screen.MainScreen.route) { Main() }

			composable(Screen.HomeScreen.route) {
				val model = hiltViewModel<HomeViewModel>()
				Home(navController, model.lastAmounts, model.amountStateFlow, model::showAmount)
			}

			composable(Screen.ProfileScreen.route) {
				val model = hiltViewModel<ProfileViewModel>()
				Profile(
					navController,
					model.userEntity,
					model.state,
					model.resourceEventFlow,
					model::onEvent
				)
			}

			composable(Screen.AccountsScreen.route) { Accounts(navController) }

			composable(Screen.AdicionarScreen.route) {
				val model = hiltViewModel<AddViewModel>()
				Add(model.resourceEventFlow, model::onEvent, model.state)
			}

			composable(Screen.HistoryScreen.route) {
				val model = hiltViewModel<HistoryViewModel>()
				val (amounts, setAmounts) = model.amounts
				Historico(
					navController,
					model.resourceEventFlow,
					amounts, setAmounts,
					model::deleteAmount,
					model::searchAmounts
				)
			}

			composable(
				Screen.ReceiptScreen.route + "/{id}",
				arguments = listOf(navArgument("id") { type = NavType.StringType })
			) { entry ->
				val id = entry.arguments?.getString("id") ?: return@composable
				val model = hiltViewModel<ReceiptViewModel>()
				model.showImage(id)
				ReceiptScreen(
					model.entityAmount,
					model.resourceEventFlow,
					model::downloadImage
				)
			}

			composable(Screen.PlusScreen.route) {
				val model = hiltViewModel<ProfileViewModel>()
				PlusScreen(navController, model::dontLoginWhenStart)
			}

			composable(Screen.SettingScreen.route) {
				val model = hiltViewModel<SettingsViewModel>()
				Setting(navController, model::saveTheme)
			}
		}

		navigation(Screen.SharedReceiptScreen.route, "extras_graph") {
			composable(
				Screen.SharedReceiptScreen.route + "/{uri}",
				arguments = listOf(navArgument("uri") { type = NavType.StringType })
			) { backStackEntry ->
				val model = hiltViewModel<AddViewModel>()
				val uri = Uri.parse(backStackEntry.arguments?.getString("uri"))
				SharedReceipt(
					navController,
					uri,
					model.resourceEventFlow,
					model.state,
					model::onEvent
				)
			}
		}
	}
}

fun easyNavigate(
	navController: NavController,
	route: String,
	stateSave: Boolean = true,
	singleTop: Boolean = true,
	restore: Boolean = true,
	include: Boolean = false,
) {
	navController.navigate(route) {
		popUpTo(navController.graph.findStartDestination().id) {
			inclusive = include
			saveState = stateSave
		}
		launchSingleTop = singleTop
		restoreState = restore
	}
}


fun formatNavigation(route: String): String {
	val r = route.split("_")[0]
	return r.plus("_graph")
}