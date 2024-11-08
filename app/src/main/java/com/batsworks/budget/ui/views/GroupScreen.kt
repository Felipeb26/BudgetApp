package com.batsworks.budget.ui.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.QRCodeImage
import com.batsworks.budget.ui.components.buttons.CustomButton
import com.batsworks.budget.ui.components.fields.CustomTextField
import com.batsworks.budget.ui.components.texts.CustomText
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.DefaultSpacer
import com.batsworks.budget.ui.theme.Padding
import com.batsworks.budget.ui.theme.customBackground

@Composable
fun GroupScreen(navController: NavController) {
	val configuration = LocalConfiguration.current
	val height = configuration.screenHeightDp.toFloat()

	var groupName by remember { mutableStateOf("https://budget/details") }
	Column(
		modifier = Modifier
			.background(customBackground)
			.fillMaxSize()
			.padding(Padding.X_MEDIUM),
		horizontalAlignment = Alignment.CenterHorizontally
	) {

		CustomTextField(
			modifier = Modifier.fillMaxWidth(),
			labelText = "Nome do grupo",
			value = groupName, readOnly = false,
			onValueChange = { groupName = it })

		ShareGroupButton(groupName)
		DefaultSpacer(Padding.EXTREM.value)
		
		CustomText(text = "")
		Box(
			modifier = Modifier
				.background(customBackground)
				.height((height / 3).dp)
				.width((height / 3).dp)
				.border(2.dp, Color400, RoundedCornerShape(10)),
		) {
			QRCodeImage(content = groupName, (height * 3).toInt())
		}
	}
}

@Composable
private fun ShareGroupButton(groupName: String) {
	val context = LocalContext.current

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(Padding.NONE, vertical = Padding.X_MEDIUM)
			.padding(horizontal = Padding.MEDIUM),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(Padding.X_MEDIUM)
	) {
		CustomButton(
			modifier = Modifier.weight(1f),
			enable = true,
			onClick = { shareQRCode(context, groupName) },
			text = "gerar qr code"
		)
		CustomButton(
			modifier = Modifier.weight(1f),
			enable = true,
			onClick = { /*TODO*/ },
			text = "gerar link "
		)
	}
}

private fun shareQRCode(context: Context, uri: String) {
	val intent = Intent(Intent.ACTION_SEND)
	intent.type = "text/plain"
	intent.putExtra(Intent.EXTRA_TEXT, uri)
	context.startActivity(Intent.createChooser(intent, "Share QR Code"))
}


@Composable
@PreviewLightDark
fun GroupScreenPreview() {
	GroupScreen(rememberNavController())
}