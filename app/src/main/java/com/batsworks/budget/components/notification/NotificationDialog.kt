package com.batsworks.budget.components.notification

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.ui.theme.Color100
import com.batsworks.budget.ui.theme.customBackground

@Composable
fun CustomDialog(
	onDismiss: () -> Unit,
	onDenay: (() -> Unit)? = null,
	onConfirm: (() -> Unit)? = null,
	text: String = stringResource(id = R.string.terms_and_conditions),
) {
	val rememberScrollState = rememberScrollState()
	val configuration = LocalConfiguration.current

	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(securePolicy = SecureFlagPolicy.SecureOn)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.border(1.dp, color = Color100, shape = RoundedCornerShape(15.dp))
				.height((configuration.screenHeightDp / 1.3).dp),
			elevation = CardDefaults.cardElevation(5.dp),
			shape = RoundedCornerShape(15.dp),
			colors = CardDefaults.cardColors(containerColor = customBackground)
		) {
			Column(
				modifier = Modifier
					.padding(0.dp)
					.padding(horizontal = 15.dp, vertical = 20.dp)
					.verticalScroll(rememberScrollState)
			) {
				CustomText(
					textStyle = MaterialTheme.typography.bodySmall,
					text = text
				)
				Row(modifier = Modifier
					.padding(0.dp)
					.padding(vertical = 20.dp)) {
					onDenay?.let {
						CustomButton(
							modifier = Modifier.weight(1f),
							enable = true, text = stringResource(id = R.string.cancel),
							onClick = it
						)
					}
					if (onDenay != null && onConfirm != null) Spacer(modifier = Modifier.width(15.dp))
					onConfirm?.let {
						CustomButton(
							modifier = Modifier.weight(1f),
							enable = true, text = stringResource(id = R.string.confirm),
							onClick = it
						)
					}
				}
			}
		}
	}
}