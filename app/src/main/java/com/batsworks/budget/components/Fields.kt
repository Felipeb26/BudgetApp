package com.batsworks.budget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.textColor

@Composable
fun CustomOutlineTextField(
	modifier: Modifier = Modifier,
	defaultText: String = "",
	enabled: Boolean = true,
	onValueChange: (String) -> Unit,
	passwordField: Boolean = false,
	labelText: String = "",
	errorMessage: String? = null,
	error: Boolean = false,
	trailingIcon: ImageVector? = null,
	leadingIcon: ImageVector? = null,
	keyboardType: KeyboardType = KeyboardType.Text,
	shape: Shape = RoundedCornerShape(30),
) {
	val (show, setShow) = remember { mutableStateOf(passwordField) }

	OutlinedTextField(
		keyboardOptions = KeyboardOptions(keyboardType = if (passwordField) KeyboardType.NumberPassword else keyboardType),
		modifier = modifier,
		value = defaultText,
		onValueChange = onValueChange,
		label = { Text(text = capitalizeStrings(labelText), color = textColor.copy(0.4f)) },
		supportingText = { Text(text = errorMessage ?: "", fontWeight = FontWeight.Bold) },
		enabled = enabled,
		singleLine = true,
		isError = error,
		colors = OutlinedTextFieldDefaults.colors(
			focusedTextColor = textColor,
			unfocusedTextColor = textColor.copy(0.4f),
			focusedContainerColor = customBackground,
			unfocusedContainerColor = customBackground.copy(0.4f),
			cursorColor = textColor,
			focusedBorderColor = Color500,
			focusedLabelColor = Color500,
			errorSupportingTextColor = MaterialTheme.colorScheme.error,
		),
		visualTransformation = if (passwordField && show) PasswordVisualTransformation() else VisualTransformation.None,
		shape = shape,
		leadingIcon = leadingIcon?.let {
			{ Icon(imageVector = it, contentDescription = "", tint = if(enabled) textColor else textColor.copy(0.4f)) }
		},
		trailingIcon = trailingIcon?.let {
			{
				Icon(
					modifier = if (enabled) Modifier.clickable { if (passwordField) setShow(!show) } else Modifier,
					imageVector = it,
					contentDescription = "",
					tint = if(enabled) textColor else textColor.copy(0.4f)
				)
			}
		}
	)
}

