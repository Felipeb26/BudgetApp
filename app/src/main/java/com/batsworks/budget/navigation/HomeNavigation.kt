package com.batsworks.budget.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.batsworks.budget.ui.view_model.add.AddViewModel
import com.batsworks.budget.ui.view_model.history.HistoryViewModel
import com.batsworks.budget.ui.view_model.home.HomeViewModel
import com.batsworks.budget.ui.view_model.profile.ProfileViewModel
import com.batsworks.budget.ui.view_model.receipt.ReceiptViewModel
import com.batsworks.budget.ui.view_model.settings.SettingsViewModel
import com.batsworks.budget.ui.views.Accounts
import com.batsworks.budget.ui.views.Add
import com.batsworks.budget.ui.views.AmountHistoryScreen
import com.batsworks.budget.ui.views.Home
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.PlusScreen
import com.batsworks.budget.ui.views.Profile
import com.batsworks.budget.ui.views.ReceiptScreen
import com.batsworks.budget.ui.views.Setting

fun NavGraphBuilder.homeNavigation(navController: NavController) {
	composable<Screen.MainScreen> { Main() }

	composable<Screen.HomeScreen> {
		val model = hiltViewModel<HomeViewModel>()
		Home(navController, model.lastAmounts, model.amountStateFlow, model::showAmount)
	}

	composable<Screen.ProfileScreen> {
		val model = hiltViewModel<ProfileViewModel>()
		Profile(
			navController,
			model.userEntity,
			model.state,
			model.resourceEventFlow,
			model::onEvent
		)
	}

	composable<Screen.AccountsScreen> { Accounts(navController) }

	composable<Screen.AdicionarScreen> {
		val model = hiltViewModel<AddViewModel>()
		Add(model.resourceEventFlow, model::onEvent, model.state)
	}

	composable<Screen.HistoryScreen> {
		val model = hiltViewModel<HistoryViewModel>()
		val (amounts, setAmounts) = model.amounts
		AmountHistoryScreen(
			navController,
			model.resourceEventFlow,
			amounts, setAmounts,
			model::deleteAmount,
			model::searchAmounts
		)
	}

	composable<Screen.ReceiptScreen> {
		val args = it.toRoute<Screen.ReceiptScreen>()
		val model = hiltViewModel<ReceiptViewModel>()

		model.showImage(args.idReceipt)
		ReceiptScreen(
			model.entityAmount,
			model.resourceEventFlow,
			model::downloadImage
		)
	}

	composable<Screen.PlusScreen> {
		val model = hiltViewModel<ProfileViewModel>()
		PlusScreen(navController, model::dontLoginWhenStart)
	}

	composable<Screen.SettingScreen> {
		val model = hiltViewModel<SettingsViewModel>()
		val coroutine = rememberCoroutineScope()
		Setting(model.user, coroutine, model::saveTheme, model::forceDataToSync)
	}
}