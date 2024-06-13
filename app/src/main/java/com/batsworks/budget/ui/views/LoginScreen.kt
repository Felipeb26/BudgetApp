package com.batsworks.budget.ui.views

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomCheckBox
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.notification.NotificationToast
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.Color200
import com.batsworks.budget.ui.theme.Loading
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.paddingScreen
import com.batsworks.budget.ui.theme.textColor
import com.batsworks.budget.ui.view_model.login.RegistrationFormEvent
import com.batsworks.budget.ui.view_model.login.SignInViewModel


@Composable
fun Login(
	navController: NavController = rememberNavController(),
	viewModel: SignInViewModel? = null,
) {
	val model = viewModel ?: viewModel<SignInViewModel>()
	val (isLoading, setLoading) = remember { mutableStateOf(false) }

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

		LoginExecution(navController, model, setLoading)
		Row(
			Modifier
				.height(IntrinsicSize.Min)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center

		) {
			CustomText(
				modifier = Modifier.clickable { navController.navigate(Screen.SignUpScreen.route) },
				text = stringResource(id = R.string.register),
				textDecoration = TextDecoration.Underline
			)
			Spacer(modifier = Modifier.width(10.dp))
			VerticalDivider(color = textColor)
			Spacer(modifier = Modifier.padding(5.dp))
			CustomText(
				text = stringResource(id = R.string.forgot_pass),
				textDecoration = TextDecoration.Underline
			)
		}
	}
	Loading(isLoading)
}

@Composable
fun LoginExecution(
	navController: NavController,
	model: SignInViewModel,
	setLoading: (Boolean) -> Unit,
) {
	val propsState = model.state
	val context = LocalContext.current
	val focusRequester = FocusRequester()
	val toast = NotificationToast(context)

	val (username, setUsername) = rememberSaveable { mutableStateOf("") }
	val (password, setPassword) = rememberSaveable { mutableStateOf("") }
	val (enterWhenLogin, setEnterWhenLogin) = remember { mutableStateOf(false) }

	LaunchedEffect(key1 = context) {
		model.validationEvents.collect { event ->
			when (event) {
				is Resource.Loading -> {
					setLoading(event.loading)
				}

				is Resource.Sucess -> {
					toast.customToast(context.getString(R.string.entering))
					easyNavigate(
						navController,
						Screen.MainScreen.route,
						stateSave = false,
						restore = false,
						include = true
					)
				}

				is Resource.Failure -> toast.customToast(event.error.plus(context.getString(R.string.user_not_found)))
			}
		}
	}

	CustomText(
		modifier = Modifier.fillMaxWidth(0.85f), text = stringResource(id = R.string.email),
		capitalize = true, textWeight = FontWeight.Bold,
		textStyle = MaterialTheme.typography.titleMedium, textDecoration = TextDecoration.Underline,
	)
	Spacer(modifier = Modifier.height(10.dp))
	CustomOutlineTextField(
		modifier = Modifier.fillMaxWidth(0.9f),
		text = username, labelText = stringResource(id = R.string.email),
		onValueChange = {
			setUsername(it)
			model.onEvent(RegistrationFormEvent.EmailChanged(it))
		},
		trailingIcon = Icons.Filled.Email,
		error = propsState.emailError != null,
		errorMessage = propsState.emailError,
		onDone = { focusRequester.requestFocus() }
	)

	CustomText(
		modifier = Modifier.fillMaxWidth(0.85f), text = stringResource(id = R.string.password),
		capitalize = true, textWeight = FontWeight.Bold,
		textStyle = MaterialTheme.typography.titleMedium, textDecoration = TextDecoration.Underline,
	)
	Spacer(modifier = Modifier.height(10.dp))
	CustomOutlineTextField(
		modifier = Modifier
			.fillMaxWidth(0.9f)
			.focusRequester(focusRequester),
		passwordField = true,
		trailingIcon = Icons.Filled.Lock,
		text = password, labelText = stringResource(id = R.string.password),
		onValueChange = {
			setPassword(it)
			model.onEvent(RegistrationFormEvent.PasswordChanged(it))
		},
		error = propsState.passwordError != null,
		errorMessage = propsState.passwordError,
	)
	Row(
		modifier = Modifier
			.fillMaxWidth(0.9f)
			.drawBehind {
				val strokeWidth = 2 * density
				val y = size.height - 1

				drawLine(
					Color200,
					Offset(35f, y),
					Offset(size.width - 40, y),
					strokeWidth
				)
			},
		verticalAlignment = Alignment.CenterVertically
	) {
		CustomCheckBox(checked = enterWhenLogin, onCheckedChange = {
			model.onEvent(RegistrationFormEvent.TermsChanged(it))
			setEnterWhenLogin(!enterWhenLogin)
		})
		CustomText(
			text = stringResource(id = R.string.login_on_enter),
			textStyle = MaterialTheme.typography.labelLarge
		)
	}

	CustomButton(modifier = Modifier
		.fillMaxWidth(0.6f)
		.padding(0.dp)
		.padding(20.dp), enable = true,
		text = stringResource(id = R.string.enter),
		onClick = { model.onEvent(RegistrationFormEvent.Submit) })
}

@Composable
@PreviewLightDark
fun LoginPreview() {
	val model = viewModel<SignInViewModel>()
	Login(rememberNavController(), model)
}

