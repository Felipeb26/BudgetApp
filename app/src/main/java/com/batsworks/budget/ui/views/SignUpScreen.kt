package com.batsworks.budget.ui.views

import android.content.Context
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomOutlineTextField
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.annotedString
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.state.login.LoginViewModel
import com.batsworks.budget.ui.state.login.RegistrationFormEvent
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.paddingScreen

@Composable
fun SignUp(navController: NavHostController, viewModel: LoginViewModel) {
	val (isLoading, setLoading) = remember { mutableStateOf(false) }

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.padding(paddingScreen())
	) {
		CustomText(text = "Cadastrar", textStyle = MaterialTheme.typography.headlineMedium)
		Spacer(modifier = Modifier.height(20.dp))
		if (!isLoading) Content(viewModel)
	}
	Loading(LocalContext.current, viewModel, navController, isLoading, setLoading)
}

@Composable
fun Content(viewModel: LoginViewModel) {
	val (nome, setNome) = remember { mutableStateOf("") }
	val (email, setEmail) = remember { mutableStateOf("") }
	val (telefone, setTelefone) = remember { mutableStateOf("") }
	val (senha, setSenha) = remember { mutableStateOf("") }
	val (confirmarSenha, setConfirmaSenha) = remember { mutableStateOf("") }
	val (checked, setChecked) = remember { mutableStateOf(false) }

	val state = viewModel.state

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
		defaultText = telefone,
		labelText = "telefone",
		onValueChange = {
			setTelefone(it)
			viewModel.onEvent(RegistrationFormEvent.TelefoneChanged(it))
		}, error = state.telefoneError != null,
		errorMessage = state.telefoneError
	)
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		defaultText = senha,
		labelText = "password",
		passwordField = true,
		onValueChange = {
			setSenha(it)
			viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))
		}, error = state.passwordError != null,
		errorMessage = state.passwordError
	)
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		defaultText = confirmarSenha,
		labelText = "confirm password",
		passwordField = true,
		onValueChange = {
			setConfirmaSenha(it)
			viewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it))
		}, error = state.repeatedPasswordErro != null,
		errorMessage = state.repeatedPasswordErro
	)
	Spacer(modifier = Modifier.height(15.dp))
	TermsAndCondition(
		checked = checked,
		checkedChange = {
			setChecked(!checked)
			viewModel.onEvent(RegistrationFormEvent.TermsChanged(it))
		}, error = state.acceptedTermsError != null,
		errorMessage = state.acceptedTermsError
	)
	Spacer(modifier = Modifier.height(15.dp))
	CustomButton(
		modifier = Modifier.fillMaxWidth(0.6f),
		text = "cadastrar",
		enable = checked,
		onClick = { viewModel.registerUser() }
	)
}

@Composable
fun TermsAndCondition(
	checked: Boolean,
	checkedChange: ((Boolean) -> Unit)?,
	error: Boolean,
	errorMessage: String? = null,
) {
	val context = LocalContext.current
	Row(
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		Checkbox(
			checked = checked,
			onCheckedChange = checkedChange,
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
			checkedChange?.invoke(!checked)
			Toast.makeText(context, "notificao", Toast.LENGTH_SHORT).show()
		}
	}
	if (error) Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
}

@Composable
fun Loading(
	context: Context,
	viewModel: LoginViewModel,
	navController: NavHostController,
	isLoading: Boolean,
	setLoading: (Boolean) -> Unit,
) {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
	LaunchedEffect(key1 = context) {
		viewModel.resourceEventFlow.collect { event ->
			when (event) {
				is Resource.Loading -> {
					setLoading(!isLoading)
					Toast.makeText(context, "carregando", Toast.LENGTH_SHORT).show()
				}

				is Resource.Failure -> {
					Toast.makeText(context, event.error, Toast.LENGTH_SHORT).show()
				}

				is Resource.Sucess -> {
					Toast.makeText(context, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT)
						.show()
					easyNavigate(navController, Screen.LoginScreen.route)
				}
			}
		}
	}

	if (isLoading) {
		LottieAnimation(
			modifier = Modifier
				.fillMaxSize()
				.background(Color800.copy(0.2f)),
			iterations = LottieConstants.IterateForever,
			composition = composition,
			speed = 0.5f
		)
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