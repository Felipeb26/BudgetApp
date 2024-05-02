package com.batsworks.budget.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.batsworks.budget.navigation.Screen

@Composable
fun Profile(navController: NavHostController) {
    Text(
        text = "login",
        modifier = Modifier.clickable {
            navController.navigate(Screen.HomeScreen.route)
        }
    )
}