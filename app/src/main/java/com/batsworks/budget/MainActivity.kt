package com.batsworks.budget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.ui.theme.BudgetTheme
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.views.Login

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color800.toArgb(), Color800.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(Color800.toArgb(), Color800.toArgb())
        )
        setContent {
            BudgetTheme {
               Login(navController = rememberNavController())
            }
        }
    }
}