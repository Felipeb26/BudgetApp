package com.batsworks.budget.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.fields.CustomTextField
import com.batsworks.budget.ui.components.buttons.CustomButton
import com.batsworks.budget.ui.theme.Padding
import com.batsworks.budget.ui.theme.customBackground

@Composable
fun GroupScreen(navController: NavController) {
	var groupName by remember { mutableStateOf("") }
	Column(
		modifier = Modifier
			.background(customBackground)
			.fillMaxSize()
			.padding(Padding.X_MEDIUM)
	) {

		CustomTextField(
			labelText = "Nome do grupo",
			value = groupName, readOnly = false,
			onValueChange = { groupName = it })

		ShareGroupButton()
	}
}

@Composable
private fun ShareGroupButton() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(Padding.NONE, vertical = Padding.X_MEDIUM)
			.padding(horizontal = Padding.MEDIUM),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(Padding.X_MEDIUM)
	) {

		CustomButton(modifier = Modifier.weight(1f), onClick = { /*TODO*/ }, text = "gerar qr code")
		CustomButton(modifier = Modifier.weight(1f), onClick = { /*TODO*/ }, text = "gerar link ")
	}
}

@Composable
@PreviewLightDark
fun GroupScreenPreview() {
	GroupScreen(rememberNavController())
}