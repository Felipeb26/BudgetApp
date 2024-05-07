package com.batsworks.budget.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.batsworks.budget.ui.state.login.LoginViewModel
import com.batsworks.budget.ui.views.Accounts
import com.batsworks.budget.ui.views.Add
import com.batsworks.budget.ui.views.Historico
import com.batsworks.budget.ui.views.Home
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.PlusScreen
import com.batsworks.budget.ui.views.Profile
import com.batsworks.budget.ui.views.Setting
import kotlin.math.sin

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
		composable(Screen.LoginScreen.route) {
			val model = viewModel<LoginViewModel>()
			Login(viewModel = model)
		}
		composable(Screen.MainScreen.route) { Main(navController) }
		composable(Screen.HomeScreen.route) { Home(navController) }
		composable(Screen.ProfileScreen.route) { Profile(navController) }
		composable(Screen.AccountsScreen.route) { Accounts(navController) }
		composable(Screen.PlusScreen.route) { PlusScreen(navController) }
		composable(Screen.SettingScreen.route) { Setting(navController) }
		composable(Screen.AdicionarScreen.route) { Add(navController) }
		composable(Screen.HistoricoScreen.route) { Historico(navController) }
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