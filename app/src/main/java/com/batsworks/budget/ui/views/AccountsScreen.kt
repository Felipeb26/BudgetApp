package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.ui.theme.customDarkBackground

@Composable
fun Accounts(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customDarkBackground)
    ) {

        CustomButton(onClick = { /*TODO*/ })
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun AccountsDark() {
    Accounts(navController = rememberNavController())
}
