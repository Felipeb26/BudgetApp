package com.batsworks.budget.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.buttons.CustomButton
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.notification.NotificationToast
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.login.RegistrationFormEvent
import com.batsworks.budget.ui.view_model.login.RegistrationFormState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun Profile(
    navController: NavHostController,
    user: StateFlow<UserEntity?>,
    state: RegistrationFormState,
    resourceEventFlow: Flow<Resource<Any>>,
    onEvent: (RegistrationFormEvent) -> Unit
) {
    val (enabled, setEnabled) = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val toast = NotificationToast(context)

    LaunchedEffect(key1 = context) {
        resourceEventFlow.collect { event ->
            when (event) {
                is Resource.Loading -> {
                    toast.show(context.getString(R.string.loading))
                }

                is Resource.Failure -> toast.show(context.getString(R.string.adding_user_error))

                is Resource.Sucess -> {
                    toast.show(context.getString(R.string.adding_user_sucess))
//                    easyNavigate(navController, Screen.LoginScreen.route)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackground)
            .padding(20.dp)
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .border(2.dp, Color800, shape = RoundedCornerShape(10))
                .padding(0.dp)
                .padding(vertical = 20.dp)
                .height(150.dp)
                .width(250.dp)
                .clickable { toast.show() },
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "profile preview",
            colorFilter = ColorFilter.tint(Color800)
        )
        Spacer(modifier = Modifier.height(30.dp))
        ProfileContent(user, enabled, state, onEvent)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(
                Modifier.weight(1f),
                onClick = { setEnabled(!enabled) },
                text = stringResource(id = R.string.update),
                enable = true
            )
            Spacer(modifier = Modifier.width(20.dp))
            CustomButton(Modifier.weight(1f), enable = enabled,
                text = stringResource(id = R.string.save),
                onClick = {
                    onEvent(RegistrationFormEvent.Submit)
//                    easyNavigate(navController, Screen.HomeScreen.route)
                })
        }
    }

}


@Composable
fun ProfileContent(
    userState: StateFlow<UserEntity?>,
    enabled: Boolean,
    state: RegistrationFormState,
    onEvent: (RegistrationFormEvent) -> Unit
) {
    val user = userState.value ?: return

    var nome by remember { mutableStateOf(user.nome) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var pass by remember { mutableStateOf(user.password.toString()) }

    CustomOutlineTextField(
        modifier = Modifier.fillMaxWidth(),
        labelText = stringResource(id = R.string.name),
        enabled = enabled, leadingIcon = Icons.Default.Person,
        text = nome, error = state.nomeError != null,
        errorMessage = state.nomeError,
        onValueChange = {
            nome = it
            onEvent(RegistrationFormEvent.NameChanged(it))
        }
    )
    CustomOutlineTextField(
        modifier = Modifier.fillMaxWidth(),
        labelText = stringResource(id = R.string.email),
        enabled = enabled, leadingIcon = Icons.Default.Email,
        text = email, error = state.emailError != null,
        errorMessage = state.emailError,
        onValueChange = {
            email = it
            onEvent(RegistrationFormEvent.EmailChanged(it))
        }
    )
    CustomOutlineTextField(
        modifier = Modifier.fillMaxWidth(),
        labelText = stringResource(id = R.string.phone),
        enabled = enabled, leadingIcon = Icons.Default.Phone,
        text = phone, error = state.telefoneError != null,
        errorMessage = state.telefoneError,
        onValueChange = {
            phone = it
            onEvent(RegistrationFormEvent.TelefoneChanged(it))
        }
    )
    CustomOutlineTextField(
        modifier = Modifier.fillMaxWidth(),
        labelText = stringResource(id = R.string.password),
        passwordField = true, enabled = enabled,
        trailingIcon = Icons.Default.Lock,
        text = user.password.toString(),
        error = state.passwordError != null,
        errorMessage = state.passwordError,
        onValueChange = {
            pass = it
            onEvent(RegistrationFormEvent.PasswordChanged(it))
        }
    )
}

@Composable
@PreviewLightDark
fun ProfilePreview() {
    val user = MutableStateFlow<UserEntity?>(
        UserEntity(
            0,
            "felipe",
            "email@email.com",
            "11971404157",
            12345678
        )
    )
    val channel = Channel<Resource<Any>>()
    Profile(
        navController = rememberNavController(),
        user.asStateFlow(),
        RegistrationFormState(),
        channel.receiveAsFlow()
    ) {}
}