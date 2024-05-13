package com.batsworks.budget.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.batsworks.budget.R

sealed class Screen(val route: String, val icon: ImageVector?, val resource: Int = 0) {
   data object LoginScreen : Screen("login_screen", Icons.Filled.Person)
   data object SignUpScreen : Screen("sign_up_screen", Icons.Filled.Person)
   data object MainScreen : Screen("main_screen", Icons.Filled.Home)
   data object SettingScreen : Screen("settings_screen", Icons.Filled.Settings)
   data object HomeScreen : Screen("home_screen", Icons.Filled.Home)
   data object HistoryScreen : Screen("history_screen", Icons.AutoMirrored.Filled.List)
   data object AdicionarScreen : Screen("add_screen", Icons.Filled.Add)
   data object ProfileScreen : Screen("profile_screen", Icons.Filled.AccountCircle)
   data object PlusScreen : Screen("plus_screen", null, R.drawable.baseline_more_horiz_24)
   data object AccountsScreen : Screen("accounts_screen", null, R.drawable.baseline_account_balance_24)

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}
