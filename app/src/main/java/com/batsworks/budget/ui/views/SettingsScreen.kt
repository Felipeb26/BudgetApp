package com.batsworks.budget.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.batsworks.budget.navigation.Screen

@Composable
fun Setting(navController: NavController) {
    Text(
        text = "configuracao",
        modifier = Modifier.clickable {
            navController.navigate(Screen.HomeScreen.route)
        }
    )
}