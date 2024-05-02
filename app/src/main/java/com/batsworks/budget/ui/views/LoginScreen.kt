package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomOutlineTextField
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.textColor

@Composable
fun Login(navController: NavController) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.padding(20.dp, 50.dp, 20.dp, 20.dp),
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

		LoginExecution()
		Row(
			Modifier
				.height(IntrinsicSize.Min)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center

		) {
			Text("cadastrar")
			Spacer(modifier = Modifier.width(10.dp))
			HorizontalDivider(
				modifier = Modifier
					.fillMaxHeight()
					.width(2.dp),
				color = Color950
			)
			Spacer(modifier = Modifier.padding(5.dp))
			Text("esqueci a senha")
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginExecution() {
	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	Text(modifier = Modifier.fillMaxWidth(0.8f), text = "username", color = textColor)
	Spacer(modifier = Modifier.height(10.dp))
	OutlinedTextField(
		modifier = Modifier.fillMaxWidth(0.8f),
		value = username,
		onValueChange = { username = it })

	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.8f),
		defaultText = username,
		onValueChange = { username = it })

	Spacer(modifier = Modifier.height(20.dp))
	Text(modifier = Modifier.fillMaxWidth(0.8f), text = "password", color = textColor)
	Spacer(modifier = Modifier.height(10.dp))
	OutlinedTextField(
		modifier = Modifier.fillMaxWidth(0.8f),
		value = password,
		onValueChange = { password = it })

	Button(colors = ButtonDefaults.buttonColors(
		containerColor = Color800,
		contentColor = Color50,
		disabledContainerColor = Color500
	),
		modifier = Modifier
			.fillMaxWidth(0.6f)
			.padding(0.dp)
			.padding(20.dp),
		enabled = false,
		onClick = { /*TODO*/ }) {
		Text(text = "entrar")
	}
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showSystemUi = true,
	showBackground = true
)
@Composable
fun LoginWhite() {
	Login(rememberNavController())
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showSystemUi = true,
	showBackground = true
)
@Composable
fun LoginDark() {
	Login(rememberNavController())
}