package com.batsworks.budget.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
	object LoginScreen : Screen("login_screen", Icons.Filled.Person)
	object SignUpScreen : Screen("sign_up_screen", Icons.Filled.Person)
	object MainScreen : Screen("main_screen", Icons.Filled.Home)
	object SettingScreen : Screen("settings_screen", Icons.Filled.Settings)
	object HomeScreen : Screen("home_screen", Icons.Filled.Home)
	object HistoricoScreen : Screen("historico_screen", Icons.AutoMirrored.Filled.List)
	object AdicionarScreen : Screen("adicionar_screen", Icons.Filled.Add)
	object ProfileScreen : Screen("profile_screen", Icons.Filled.AccountCircle)

	fun withArgs(vararg args: String): String {
		return buildString {
			append(route)
			args.forEach { arg -> append("/$arg") }
		}
	}
}
