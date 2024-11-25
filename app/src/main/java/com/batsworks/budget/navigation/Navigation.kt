package com.batsworks.budget.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
		modifier = if (paddingValues != null) Modifier.padding(paddingValues) else Modifier,
		exitTransition = {
			slideOutOfContainer(
				AnimatedContentTransitionScope.SlideDirection.Left,
				tween(1500)
			) + fadeOut()
		}, popEnterTransition = {
			slideIntoContainer(
				AnimatedContentTransitionScope.SlideDirection.Right,
				tween(1500)
			) + fadeIn()
		}
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