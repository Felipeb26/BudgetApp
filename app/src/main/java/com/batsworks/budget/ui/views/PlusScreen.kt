package com.batsworks.budget.ui.views

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.formatScreenTitle
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950
import com.batsworks.budget.ui.theme.CustomLottieAnimation
import com.batsworks.budget.ui.theme.customBackground
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun PlusScreen(
    navController: NavHostController,
    dontLoginWhenStart: () -> Unit,
) {
    val screens = listOf(Screen.SettingScreen, Screen.AccountsScreen)
    val (exit, makeExit) = remember { mutableStateOf(false) }

    if (exit) ExitAnimation(true)
    else Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackground),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        screens.forEachIndexed { _, screen ->
            CardFunction(navController, screen, screen.icon, screen.resource)
        }
        CardFunction(
            navController,
            screenRoute = null,
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            function = {
                dontLoginWhenStart()
                makeExit(true)
            })
    }
}

@Composable
fun CardFunction(
    navController: NavHostController,
    screenRoute: Screen?,
    icon: ImageVector? = null,
    resource: Int? = null,
    function: (() -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(60.dp)
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(25),
        colors = CardDefaults.cardColors(
            containerColor = Color800,
            contentColor = Color950
        ),
        onClick = function ?: {
            easyNavigate(
                navController,
                screenRoute?.route ?: Screen.HomeScreen.route
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, color = Color400, RoundedCornerShape(25)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(35.dp))
            (icon ?: resource?.let { ImageVector.vectorResource(it) })?.let {
                Icon(
                    imageVector = it,
                    contentDescription = "",
                    tint = Color50
                )
            }
            Spacer(modifier = Modifier.width(35.dp))
            CustomText(
                text = formatScreenTitle(screenRoute),
                color = Color50, textWeight = FontWeight.Bold,
                textStyle = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun ExitAnimation(exit: Boolean) {
    val activity = (LocalContext.current as? Activity)
    CustomLottieAnimation(lottieComposition = R.raw.bye, show = exit)
    Timer().schedule(2500) {
        activity?.finish()
    }
}

@Composable
@PreviewLightDark
fun PlusPreview() {
    PlusScreen(rememberNavController()) {}
}