package com.batsworks.budget.ui.views

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomOutlineTextField
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.annotedString
import com.batsworks.budget.ui.state.login.LoginViewModel
import com.batsworks.budget.ui.state.login.RegistrationFormEvent
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.paddingScreen

@Composable
fun SignUp(navController: NavHostController, viewModel: LoginViewModel) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.padding(paddingScreen())
	) {
		CustomText(text = "Cadastrar", textStyle = MaterialTheme.typography.headlineMedium)
		Spacer(modifier = Modifier.height(20.dp))
		Content(navController, viewModel)
	}
}

@Composable
fun Content(navController: NavHostController, viewModel: LoginViewModel) {
	val (nome, setNome) = remember { mutableStateOf("") }
	val (email, setEmail) = remember { mutableStateOf("") }
	val (telefone, setTelefone) = remember { mutableStateOf("") }
	val (senha, setSenha) = remember { mutableStateOf("") }
	val (confirmarSenha, setConfirmaSenha) = remember { mutableStateOf("") }
	val (checked, setChecked) = remember { mutableStateOf(false) }

	val context = LocalContext.current
	val state = viewModel.state

	LaunchedEffect(key1 = context) {
		viewModel.validationEvents.collect { event ->
			when (event) {
				is LoginViewModel.ValidationEvent.Sucess -> {
					Toast.makeText(context, "deu certo", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		defaultText = nome,
		labelText = "nome",
		onValueChange = {
			setNome(it)
			viewModel.onEvent(RegistrationFormEvent.NameChanged(it))
		}, error = state.nomeError != null,
		errorMessage = state.nomeError
	)
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		defaultText = email,
		labelText = "email",
		onValueChange = {
			setEmail(it)
			viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))
		}, error = state.emailError != null,
		errorMessage = state.emailError
	)
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		onValueChange = { setTelefone(it) },
		defaultText = telefone,
		labelText = "text"
	)
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		onValueChange = { setSenha(it) },
		defaultText = senha,
		labelText = "password"
	)
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		onValueChange = { setConfirmaSenha(it) },
		defaultText = confirmarSenha,
		labelText = "confirm password"
	)
	Spacer(modifier = Modifier.height(15.dp))
	TermsAndCondition(checked, setChecked)

	Spacer(modifier = Modifier.height(15.dp))
	CustomButton(
		modifier = Modifier.fillMaxWidth(0.6f),
		text = "cadastrar",
		enable = checked,
		onClick = { viewModel.onEvent(RegistrationFormEvent.Submit) }
	)
//		onClick = { navController.navigate(Screen.LoginScreen.route) })
}

@Composable
fun TermsAndCondition(checked: Boolean, setChecked: (Boolean) -> Unit) {
	val context = LocalContext.current

	Row(
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		Checkbox(
			checked = checked,
			onCheckedChange = { setChecked(it) },
			colors = CheckboxDefaults.colors(
				checkmarkColor = Color800,
				checkedColor = Color600
			)
		)
		val annoted = annotedString(
			"Eu concordo com", " e ",
			"a privacidade", "a politica"
		)
		ClickableText(
			text = annoted
		) { _ ->
			setChecked(!checked)
			Toast.makeText(context, "notificao", Toast.LENGTH_SHORT).show()
		}
	}
}


@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true
)
@Composable
fun SignUpDark() {
	val model = viewModel<LoginViewModel>()
	SignUp(rememberNavController(), model)
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showBackground = true
)
@Composable
fun SignUpWhite() {
	val model = viewModel<LoginViewModel>()
	SignUp(rememberNavController(), model)
}
//https://www.youtube.com/watch?v=zu8lQSVw4vk