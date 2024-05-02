package com.batsworks.budget.ui.views

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.navigation.Navigate
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color900
import com.batsworks.budget.ui.theme.Color950

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }) { paddingValues ->
        Navigate(navController, Screen.HomeScreen, paddingValues)
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val screens =
        listOf(Screen.HomeScreen, Screen.HistoricoScreen, Screen.ProfileScreen, Screen.SettingScreen)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = Color700) {
        screens.forEachIndexed { index, screen ->
            if (index == 2) FloatingButton(navController)

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color900,
                    selectedTextColor = Color900,
                    indicatorColor = Color500,
                    unselectedTextColor = Color50,
                    unselectedIconColor = Color50
                ),
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                icon = { Icon(imageVector = screen.icon, contentDescription = "") },
                label = {
                    Text(
                        text = formatScreenTitle(screen.route),
                        fontWeight = FontWeight.Bold
                    )
                },
                onClick = {
                    Log.d("12", screen.route)
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}

@Composable
fun FloatingButton(navController: NavController) {
    FloatingActionButton(
        modifier = Modifier.border(2.dp, Color800, RoundedCornerShape(30.dp)),
        shape = RoundedCornerShape(30.dp),
        containerColor = Color300,
        contentColor = Color950,
        onClick = { easyNavigate(navController, Screen.AdicionarScreen.route) }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "", tint = Color50)
    }
}

fun formatScreenTitle(title: String): String {
    return title.replace("_screen", "").toUpperCase(Locale.current)
}

fun easyNavigate(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}