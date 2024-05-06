package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950
import com.batsworks.budget.ui.theme.customDarkBackground

@Composable
fun PlusScreen(navController: NavHostController) {

    val screens = listOf(Screen.SettingScreen, Screen.AccountsScreen)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customDarkBackground),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        screens.forEachIndexed { _, screen ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(25),
                colors = CardDefaults.cardColors(
                    containerColor = Color800,
                    contentColor = Color950
                ),
                onClick = { easyNavigate(navController, screen.route) }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(30.dp))
                    Icon(
                        imageVector = screen.icon ?: ImageVector.vectorResource(screen.resource),
                        contentDescription = "",
                        tint = Color50
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    CustomText(text = formatScreenTitle(screen.route), color = Color50)
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PlusWhite() {
    PlusScreen(rememberNavController())
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PlusDark() {
    PlusScreen(rememberNavController())
}