package com.batsworks.budget.ui.views

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.formatScreenTitle
import com.batsworks.budget.navigation.Navigate
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950

@Composable
fun Main(navController: NavHostController = rememberNavController()) {
	Scaffold(
		bottomBar = { BottomBar(navController) }) { paddingValues ->
		Navigate(navController, Screen.HomeScreen.route, paddingValues)
	}
}

@Composable
fun BottomBar(navController: NavController) {
	val screens = listOf(
		Screen.HomeScreen,
		Screen.HistoryScreen,
		Screen.ProfileScreen,
		Screen.PlusScreen
	)
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination

	NavigationBar(containerColor = Color700) {
		screens.forEachIndexed { index, screen ->
			if (index == 2) FloatingButton(navController)

			NavigationBarItem(
				colors = NavigationBarItemDefaults.colors(
					selectedIconColor = Color50,
					selectedTextColor = Color50,
					indicatorColor = Color800,
					unselectedTextColor = Color300,
					unselectedIconColor = Color300
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
						text = formatScreenTitle(screen),
						textStyle = MaterialTheme.typography.labelSmall,
						textWeight = FontWeight.Bold,
						color = Color50
					)
				},
				onClick = { easyNavigate(navController, screen.route) })
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


@Composable
@PreviewLightDark
fun MainPreview() {
	Main()
}
