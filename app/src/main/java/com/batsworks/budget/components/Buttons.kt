package com.batsworks.budget.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color800

@Composable
fun CustomButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit, text: String = "not implemented", enable: Boolean = false,
	shape: Shape = RoundedCornerShape(30),
	textStyle: TextStyle = MaterialTheme.typography.labelLarge,
	containerColor: Color = Color800,
	borderColor: Color = Color600,
) {
	Button(
		modifier = modifier, border = BorderStroke(2.dp, borderColor),
		shape = shape,
		colors = ButtonDefaults.buttonColors(
			containerColor = containerColor,
			contentColor = Color50,
			disabledContainerColor = Color500,
			disabledContentColor = borderColor
		), onClick = onClick, enabled = enable
	) {
		CustomText(
			textStyle = textStyle,
			text = text.toUpperCase(Locale.current),
			color = if (!enable) Color600 else Color.White,
			textWeight = FontWeight.Bold
		)
	}
}

@Composable
fun CustomIconButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	enabled: Boolean = true,
	containerColor: Color = Color800.copy(0.8f),
	contentColor: Color = Color50,
	imageVector: ImageVector,
) {
	IconButton(
		modifier = modifier,
		enabled = enabled,
		onClick = onClick,
		colors = IconButtonColors(
			containerColor = containerColor,
			contentColor = contentColor,
			disabledContainerColor = Color500,
			disabledContentColor = Color400
		)
	) {
		Icon(
			imageVector = imageVector,
			contentDescription = "icon button",
			tint = contentColor
		)
	}
}

@Composable
fun CustomCheckBox(
	checked: Boolean,
	onCheckedChange: ((Boolean) -> Unit)?,
) {
	Checkbox(
		checked = checked,
		onCheckedChange = onCheckedChange,
		colors = CheckboxDefaults.colors(
			checkmarkColor = Color800,
			checkedColor = Color400
		)
	)
}