package com.batsworks.budget.ui.views

import android.app.Activity
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.formatScreenTitle
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950
import com.batsworks.budget.ui.theme.customDarkBackground
import com.batsworks.budget.ui.view_model.profile.ProfileViewModel
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun PlusScreen(
	navController: NavHostController,
	model: ProfileViewModel = viewModel<ProfileViewModel>(),
) {
	val screens = listOf(Screen.SettingScreen, Screen.AccountsScreen)
	val (exit, makeExit) = remember { mutableStateOf(false) }
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customDarkBackground),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Spacer(modifier = Modifier.height(10.dp))
		screens.forEachIndexed { _, screen ->
			CardFunction(navController, screen.route, screen.icon, screen.resource)
		}
		CardFunction(
			navController,
			screenRoute = "exit",
			icon = Icons.AutoMirrored.Filled.ExitToApp,
			function = {
				model.dontLoginWhenStart()
				makeExit(!exit)
			})
	}
	if (exit) ExitAnimation()
}

@Composable
fun CardFunction(
	navController: NavHostController,
	screenRoute: String,
	icon: ImageVector? = null,
	resource: Int? = null,
	function: (() -> Unit)? = null,
) {
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
		onClick = function ?: { easyNavigate(navController, screenRoute) }
	) {
		Row(
			modifier = Modifier.fillMaxSize(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start
		) {
			Spacer(modifier = Modifier.width(30.dp))
			(icon ?: resource?.let { ImageVector.vectorResource(it) })?.let {
				Icon(
					imageVector = it,
					contentDescription = "",
					tint = Color50
				)
			}
			Spacer(modifier = Modifier.width(30.dp))
			CustomText(text = formatScreenTitle(screenRoute), color = Color50)
		}
	}
}

@Composable
fun ExitAnimation() {
	val activity = (LocalContext.current as? Activity)

	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bye))
	LottieAnimation(
		modifier = Modifier
			.fillMaxSize()
			.background(Color800),
		iterations = LottieConstants.IterateForever,
		composition = composition,
		speed = 0.5f
	)
	Timer().schedule(2500) {
		activity?.finish()
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