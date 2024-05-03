package com.batsworks.budget.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.batsworks.budget.ui.views.Adicionar
import com.batsworks.budget.ui.views.Historico
import com.batsworks.budget.ui.views.Home
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.Profile
import com.batsworks.budget.ui.views.Setting

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
		composable(Screen.LoginScreen.route) { Login(navController) }
		composable(Screen.MainScreen.route) { Main(navController) }
		composable(Screen.HomeScreen.route) { Home(navController) }
		composable(Screen.ProfileScreen.route) { Profile(navController) }
		composable(Screen.SettingScreen.route) { Setting(navController) }
		composable(Screen.AdicionarScreen.route) { Adicionar(navController) }
		composable(Screen.HistoricoScreen.route) { Historico(navController) }
	}
}