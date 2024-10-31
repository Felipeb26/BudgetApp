package com.batsworks.budget.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigate(
	navController: NavHostController = rememberNavController(),
	screen: Screen = Screen.LoginScreen,
	paddingValues: PaddingValues? = null,
) {

	NavHost(
		navController = navController,
		startDestination = screen,
		modifier = if (paddingValues != null) Modifier.padding(paddingValues) else Modifier
	){
		authNavigation(navController)
		homeNavigation(navController)
		resourcesViewNavigation(navController)
	}
}

fun easyNavigate(
	navController: NavController,
	route: Screen,
	stateSave: Boolean = true,
	singleTop: Boolean = stateSave,
	restore: Boolean = stateSave,
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

//fun easyNavigate(
//	navController: NavController,
//	route: String,
//	stateSave: Boolean = true,
//	singleTop: Boolean = stateSave,
//	restore: Boolean = stateSave,
//	include: Boolean = false,
//) {
//	navController.navigate(route) {
//		popUpTo(navController.graph.findStartDestination().id) {
//			inclusive = include
//			saveState = stateSave
//		}
//		launchSingleTop = singleTop
//		restoreState = restore
//	}
//}
//
//
//fun formatNavigation(route: String): String {
//	val r = route.split("_")[0]
//	return r.plus("_graph")
//}