package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomOutlineTextField
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.state.LoginViewModel
import com.batsworks.budget.ui.state.factoryProvider
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.paddingScreen
import com.batsworks.budget.ui.theme.textColor

@Composable
fun Login(navController: NavController) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.padding(paddingScreen()),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			modifier = Modifier
				.padding(0.dp)
				.padding(0.dp, 45.dp),
			painter = painterResource(id = R.drawable.logo),
			contentDescription = ""
		)
		Spacer(modifier = Modifier.height(50.dp))

		val viewModel = viewModel<LoginViewModel>(
			factory = LoginViewModel::class.qualifiedName?.let { factoryProvider(it) }
		)
		LoginExecution(navController, viewModel)
		Row(
			Modifier
				.height(IntrinsicSize.Min)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center

		) {
			CustomText(text = "cadastrar", textDecoration = TextDecoration.Underline)
			Spacer(modifier = Modifier.width(10.dp))
			VerticalDivider(color = textColor)
			Spacer(modifier = Modifier.padding(5.dp))
			CustomText(
				modifier = Modifier.clickable { navController.navigate(Screen.SignUpScreen.route) },
				text = "esqueci a senha", textDecoration = TextDecoration.Underline)
		}
	}
}

@Composable
fun LoginExecution(navController: NavController, viewModel: LoginViewModel) {
	val (username, setUsername) = remember { mutableStateOf("") }
	val (password, setPassword) = remember { mutableStateOf("") }

	CustomText(
		modifier = Modifier.fillMaxWidth(0.8f), text = "username", capitalize = true,
		textStyle = MaterialTheme.typography.titleMedium, textDecoration = TextDecoration.Underline
	)
	Spacer(modifier = Modifier.height(10.dp))
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.8f),
		defaultText = username, labelText = "Email",
		onValueChange = { setUsername(it) }, trailingIcon = Icons.Filled.Email
	)

	CustomText(
		modifier = Modifier.fillMaxWidth(0.8f), text = "password", capitalize = true,
		textStyle = MaterialTheme.typography.titleMedium, textDecoration = TextDecoration.Underline
	)
	Spacer(modifier = Modifier.height(10.dp))
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.8f),
		passwordField = true,
		trailingIcon = Icons.Filled.Lock,
		defaultText = password, labelText = "Password",
		onValueChange = { setPassword(it) })

	CustomButton(modifier = Modifier
		.fillMaxWidth(0.6f)
		.padding(0.dp)
		.padding(20.dp), enable = true, onClick = {
		viewModel.log()
		navController.navigate(Screen.MainScreen.route) {
			popUpTo(gotoRoute(Screen.MainScreen.route))
		}
	})
}

private fun gotoRoute(text: String): String {
	return text.substring(0, text.indexOf("_")).toLowerCase(Locale.current)
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true
)
@Composable
fun LoginDark() {
	Login(rememberNavController())
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showBackground = true
)
@Composable
fun LoginWhite() {
	Login(rememberNavController())
}

