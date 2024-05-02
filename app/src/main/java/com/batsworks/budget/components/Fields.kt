package com.batsworks.budget.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.batsworks.budget.ui.theme.textColor

@Composable
fun CustomOutlineTextField(
	modifier: Modifier = Modifier,
	defaultText: String = "",
	enabled: Boolean = true,
	onValueChange: (String) -> Unit,
	passwordField: Boolean = true,
	errorMessage: String = "",
	error: Boolean = false,
) {
	var show by remember { mutableStateOf(!passwordField) }

	OutlinedTextField(
		modifier = modifier,
		value = defaultText,
		onValueChange = onValueChange,
		supportingText = { Text(text = errorMessage) },
		enabled = enabled,
		singleLine = true,
		isError = error,
		colors = TextFieldDefaults.colors(
			focusedTextColor = textColor,
			unfocusedTextColor = textColor.copy(0.4f)
		),
		visualTransformation = if (passwordField && show) PasswordVisualTransformation() else VisualTransformation.None,
		trailingIcon = {
			Icon(
				modifier = Modifier.clickable { show = !show },
				imageVector = Icons.Filled.ThumbUp,
				contentDescription = ""
			)
		}
	)
}