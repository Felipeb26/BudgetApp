package com.batsworks.budget.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.batsworks.budget.domain.dao.Collection
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.ui.view_model.add.AddViewModel
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.history.HistoryViewModel
import com.batsworks.budget.ui.view_model.home.HomeViewModel
import com.batsworks.budget.ui.view_model.profile.ProfileViewModel
import com.batsworks.budget.ui.view_model.receipt.ReceiptViewModel
import com.batsworks.budget.ui.view_model.settings.SettingsViewModel
import com.batsworks.budget.ui.views.Accounts
import com.batsworks.budget.ui.views.Add
import com.batsworks.budget.ui.views.Historico
import com.batsworks.budget.ui.views.Home
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.PlusScreen
import com.batsworks.budget.ui.views.Profile
import com.batsworks.budget.ui.views.ReceiptScreen
import com.batsworks.budget.ui.views.Setting
import com.batsworks.budget.ui.views.SharedReceipt

@Composable
fun Navigate(
	navController: NavHostController,
	screen: Screen,
	paddingValues: PaddingValues? = null,
) {
	NavHost(
		navController = navController,
		startDestination = screen.route,
		modifier = if (paddingValues != null) Modifier.padding(paddingValues) else Modifier
	) {
		composable(Screen.MainScreen.route) { Main(navController) }
		composable(Screen.HomeScreen.route) {
			val model = viewModel<HomeViewModel>()
			Home(navController, model.lastAmounts, model.amountStateFlow, model::showAmount)
		}
		composable(Screen.ProfileScreen.route) {
			val model = viewModel<ProfileViewModel>(
				factory = factoryProvider(
					ProfileViewModel(
						repository = CustomRepository(
							Collection.USERS.path,
							UserEntity::class.java
						)
					)
				)
			)
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
			Add(model.resourceEventFlow, model::onEvent, model.state)
		}
		composable(Screen.HistoryScreen.route) {
			val model = viewModel<HistoryViewModel>()
			Historico(
				navController,
				model.resourceEventFlow,
				model.amounts,
				model::onInit,
				model::deleteAmount
			)
		}
		composable(
			Screen.ReceiptScreen.route + "/{id}",
			arguments = listOf(navArgument("id") {
				type = NavType.StringType
				defaultValue = "0"
				nullable = false
			})
		) { entry ->
			val context = LocalContext.current
			val model = viewModel<ReceiptViewModel>(
				factory = factoryProvider(
					ReceiptViewModel(
						context = context,
						id = entry.arguments?.getString("id") ?: "0"
					)
				)
			)
			ReceiptScreen(
				model.entityAmount,
				model.resourceEventFlow,
				model::downloadImage
			)
		}

		composable(Screen.PlusScreen.route) {
			val model = viewModel<ProfileViewModel>()
			PlusScreen(navController, model::dontLoginWhenStart)
		}

		composable(Screen.SettingScreen.route) {
			val model = viewModel<SettingsViewModel>()
			Setting(navController, model::saveTheme)
		}

		composable(
			Screen.SharedReceiptScreen.route+"/{uri}",
			arguments = listOf(navArgument("file") { type = NavType.StringType })
		) { backStackEntry ->
			val uri = Uri.parse(backStackEntry.arguments?.getString("uri"))
			SharedReceipt(uri)
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

private fun goTo(route: String): String {
	return route.lowercase().substring(0, route.indexOf("_"))
}