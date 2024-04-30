package com.batsworks.budget.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.Main

@Composable
fun Navigate(screen: Screen) {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = screen.route) {
        composable(Screen.MainScreen.route) {
            Main(navHostController)
        }
        composable(Screen.LoginScreen.route) {
            Login(navHostController)
        }
    }

}