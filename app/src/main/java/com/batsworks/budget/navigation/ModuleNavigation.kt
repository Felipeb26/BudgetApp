package com.batsworks.budget.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main
import com.batsworks.budget.ui.views.SignUp

@Composable
fun StartNavigate(
	navController: NavHostController,
	screen: Screen,
) {
	NavHost(
		navController = navController,
		startDestination = screen.route,
		modifier = Modifier.background(customBackground)
	) {
		composable(Screen.LoginScreen.route) { Login(navController) }
		composable(Screen.SignUpScreen.route) { SignUp(navController)}
		navigation(Screen.MainScreen.route, route = "main") {
			composable(Screen.MainScreen.route) { Main(navController) }
		}
	}
}
