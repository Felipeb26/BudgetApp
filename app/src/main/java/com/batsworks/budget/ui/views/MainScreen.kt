package com.batsworks.budget.ui.views

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.navigation.Navigate
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color900
import com.batsworks.budget.ui.theme.Color950

@Composable
fun Main(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomBar(navController) }) { paddingValues ->
        Navigate(navController, Screen.HomeScreen, paddingValues)
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val screens =
        listOf(
            Screen.HomeScreen,
            Screen.HistoricoScreen,
            Screen.ProfileScreen,
            Screen.PlusScreen
        )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = Color700) {
        screens.forEachIndexed { index, screen ->
            if (index == 2) {
                FloatingButton(navController)
            }

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color900,
                    selectedTextColor = Color900,
                    indicatorColor = Color500,
                    unselectedTextColor = Color50,
                    unselectedIconColor = Color50
                ),
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                icon = {
                    Icon(
                        imageVector = screen.icon ?: ImageVector.vectorResource(screen.resource),
                        contentDescription = ""
                    )
                },
                label = {
                    CustomText(
                        text = formatScreenTitle(screen.route),
                        textStyle = MaterialTheme.typography.labelSmall,
                        textWeight = FontWeight.Bold,
                        color = Color50
                    )
                },
                onClick = {
                    Log.d("12", screen.route)
                    easyNavigate(navController, screen.route)
                })
        }
    }
}

@Composable
fun FloatingButton(navController: NavController, modifier: Modifier = Modifier) {
    FloatingActionButton(
        modifier = modifier.padding(0.dp, 0.dp, 0.dp, 20.dp),
        shape = RoundedCornerShape(30.dp),
        elevation = FloatingActionButtonDefaults.elevation(2.dp, 3.dp),
        containerColor = Color300,
        contentColor = Color950,
        onClick = { easyNavigate(navController, Screen.AdicionarScreen.route) }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "", tint = Color50)
    }
}

fun formatScreenTitle(title: String): String {
    return title.replace("_screen", "").toUpperCase(Locale.current)
}

@Preview
@Composable
fun MainWhite() {
    Main()
}