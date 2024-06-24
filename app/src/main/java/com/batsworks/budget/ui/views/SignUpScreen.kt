package com.batsworks.budget.ui.views

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.buttons.CustomButton
import com.batsworks.budget.components.buttons.CustomCheckBox
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.animations.Loading
import com.batsworks.budget.components.annotedString
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.fields.CustomPasswordOutlineTextField
import com.batsworks.budget.components.notification.CustomDialog
import com.batsworks.budget.components.notification.NotificationToast
import com.batsworks.budget.components.visual_transformation.PhoneTransformation
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.paddingScreen
import com.batsworks.budget.ui.view_model.login.LoginViewModel
import com.batsworks.budget.ui.view_model.login.RegistrationFormEvent

@Composable
fun SignUp(navController: NavHostController, viewModel: LoginViewModel) {
    val (isLoading, setLoading) = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val toast = NotificationToast(context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(customBackground)
            .padding(paddingScreen())
    ) {
        CustomText(
            text = stringResource(id = R.string.register),
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                letterSpacing = TextUnit(
                    0.7f,
                    TextUnitType.Sp
                )
            ),
            isUpperCase = true, textDecoration = TextDecoration.Underline,
            textWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (!isLoading) Content(viewModel)
    }

    LaunchedEffect(key1 = context) {
        viewModel.resourceEventFlow.collect { event ->
            when (event) {
                is Resource.Loading -> {
                    setLoading(!isLoading)
                    toast.show(context.getString(R.string.loading))
                }

                is Resource.Failure -> toast.show(context.getString(R.string.adding_user_error))

                is Resource.Sucess -> {
                    toast.show(context.getString(R.string.adding_user_sucess))
                    easyNavigate(navController, Screen.LoginScreen.route)
                }
            }
        }
    }
    Loading(isLoading)
}

@Composable
private fun Content(viewModel: LoginViewModel) {
    val (nome, setNome) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (telefone, setTelefone) = remember { mutableStateOf("") }
    val (senha, setSenha) = remember { mutableStateOf("") }
    val (confirmarSenha, setConfirmaSenha) = remember { mutableStateOf("") }
    val (checked, setChecked) = remember { mutableStateOf(false) }


    val state = viewModel.state

    CustomOutlineTextField(
        modifier = Modifier.fillMaxWidth(0.9f),
        leadingIcon = Icons.Default.Person,
        text = nome,
        labelText = stringResource(id = R.string.name),
        onValueChange = {
            setNome(it)
            viewModel.onEvent(RegistrationFormEvent.NameChanged(it))
        }, error = state.nomeError != null,
        errorMessage = state.nomeError
    )
    CustomOutlineTextField(
        modifier = Modifier.fillMaxWidth(0.9f),
        leadingIcon = Icons.Default.Email,
        text = email,
        labelText = stringResource(id = R.string.email),
        onValueChange = {
            setEmail(it)
            viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))
        }, error = state.emailError != null,
        errorMessage = state.emailError
    )
    CustomOutlineTextField(
        modifier = Modifier.fillMaxWidth(0.9f),
        leadingIcon = Icons.Default.Phone,
        keyboardType = KeyboardType.Number,
        transformation = PhoneTransformation(),
        text = telefone,
        labelText = stringResource(id = R.string.phone),
        onValueChange = {
            setTelefone(it)
            viewModel.onEvent(RegistrationFormEvent.TelefoneChanged(it))
        }, error = state.telefoneError != null,
        errorMessage = state.telefoneError
    )
    CustomPasswordOutlineTextField(
        modifier = Modifier.fillMaxWidth(0.9f),
        text = senha,
        labelText = stringResource(id = R.string.password),
        passwordField = true,
        onValueChange = {
            setSenha(it)
            viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))
        }, error = state.passwordError != null,
        errorMessage = state.passwordError
    )
    CustomPasswordOutlineTextField(
        modifier = Modifier.fillMaxWidth(0.9f),
        text = confirmarSenha,
        labelText = stringResource(id = R.string.confirm_pass),
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
        checkedChange = setChecked,
        onCheckedChange = { viewModel.onEvent(RegistrationFormEvent.TermsChanged(it)) },
        error = state.acceptedTermsError != null,
        errorMessage = state.acceptedTermsError
    )
    Spacer(modifier = Modifier.height(15.dp))
    CustomButton(
        modifier = Modifier.fillMaxWidth(0.6f),
        text = stringResource(id = R.string.register),
        enable = checked,
        onClick = { viewModel.onEvent(RegistrationFormEvent.Submit) }
    )
}

@Composable
fun TermsAndCondition(
    checked: Boolean,
    checkedChange: (Boolean) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    error: Boolean,
    errorMessage: String? = null,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) CustomDialog(
        onDismiss = { showDialog = !showDialog },
        onConfirm = {
            showDialog = !showDialog
            checkedChange.invoke(true)
            onCheckedChange.invoke(true)
        },
        onDenay = {
            showDialog = !showDialog
            checkedChange.invoke(false)
            onCheckedChange.invoke(false)
        })

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckBox(checked = checked, onCheckedChange = checkedChange)

        val annoted = annotedString(
            stringResource(id = R.string.i_agree_with), stringResource(id = R.string.and),
            stringResource(id = R.string.the_privacy), stringResource(id = R.string.the_policiy)
        )
        ClickableText(text = annoted) { _ -> showDialog = !showDialog }
    }
    if (error) Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
}


@PreviewLightDark
@Composable
fun SignUpWhite() {
    val model = viewModel<LoginViewModel>()
    SignUp(rememberNavController(), model)
}
