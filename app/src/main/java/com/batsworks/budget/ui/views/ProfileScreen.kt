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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.notification.CustomToast
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customDarkBackground
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.profile.ProfileViewModel

@Composable
fun Profile(
	navController: NavHostController,
	viewModel: ProfileViewModel = viewModel<ProfileViewModel>(),
) {
	val (enabled, setEnabled) = remember { mutableStateOf(false) }
	val context = LocalContext.current

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customDarkBackground)
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
				.clickable {
					CustomToast(context, "item não implementado ainda")
				},
			painter = painterResource(id = R.drawable.logo_resource),
			contentDescription = "profile preview"
		)
		Spacer(modifier = Modifier.height(30.dp))
		ProfileContent(viewModel, enabled)
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			CustomButton(
				Modifier.weight(1f),
				onClick = { setEnabled(!enabled) },
				text = "atualizar",
				enable = true
			)
			Spacer(modifier = Modifier.width(20.dp))
			CustomButton(Modifier.weight(1f), onClick = { /*TODO*/ }, text = "salvar")
		}
	}

}


@Composable
fun ProfileContent(viewModel: ProfileViewModel, enabled: Boolean) {
	val userState = viewModel.userEntity.collectAsState()
	val user = userState.value ?: return
	val modifier = Modifier.fillMaxWidth()

	CustomOutlineTextField(
		modifier = modifier,
		onValueChange = {},
		labelText = "name",
		defaultText = user.nome,
		enabled = enabled,
		leadingIcon = Icons.Default.Person
	)
	CustomOutlineTextField(
		modifier = modifier,
		onValueChange = {},
		labelText = "email",
		defaultText = user.email,
		enabled = enabled,
		leadingIcon = Icons.Default.Email
	)
	CustomOutlineTextField(
		modifier = modifier,
		onValueChange = {},
		labelText = "phone",
		defaultText = user.phone,
		enabled = enabled,
		leadingIcon = Icons.Default.Phone
	)
	CustomOutlineTextField(
		modifier = modifier,
		onValueChange = {},
		labelText = "password",
		passwordField = true,
		enabled = enabled,
		defaultText = user.password.toString(),
		trailingIcon = Icons.Default.Lock
	)
}

@Composable
@PreviewLightDark
fun ProfilePreview() {
	val model = viewModel<ProfileViewModel>(
		factory = factoryProvider(ProfileViewModel())
	)
	Profile(navController = rememberNavController(), model)
}