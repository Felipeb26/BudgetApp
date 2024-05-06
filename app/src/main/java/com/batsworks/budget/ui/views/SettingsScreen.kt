package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.theme.customDarkBackground

@Composable
fun Setting(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customDarkBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomText(
            text = "configuracao",
            modifier = Modifier.clickable {
                navController.navigate(Screen.LoginScreen.route)
            }
        )

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingDark() {
    Setting(navController = rememberNavController())
}
@Preview(showBackground = true)
@Composable
fun SettingWhite() {
    Setting(navController = rememberNavController())
}