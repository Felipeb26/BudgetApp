package com.batsworks.budget.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.ui.components.buttons.CustomButton
import com.batsworks.budget.ui.theme.customBackground

@Composable
fun Accounts(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackground)
    ) {

        CustomButton(onClick = { /*TODO*/ })
    }
}


@Composable
@PreviewLightDark
fun AccountsPreview() {
    Accounts(navController = rememberNavController())
}
