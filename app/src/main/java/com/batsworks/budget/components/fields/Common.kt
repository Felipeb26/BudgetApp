package com.batsworks.budget.components.fields

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.batsworks.budget.components.functions.composeBool
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color700
import java.util.Locale


@Composable
fun CustomTextField(
	modifier: Modifier = Modifier,
	value: String = "",
	onValueChange: (String) -> Unit,
	trailingIcon: @Composable (() -> Unit)? = null,
	isUpper: Boolean = false,
	colores: TextFieldColors = TextFieldDefaults.colors(
		focusedTextColor = Color50,
		unfocusedTextColor = Color50,
		focusedContainerColor = composeBool(isSystemInDarkTheme(), Color400, Color700),
		unfocusedContainerColor = composeBool(isSystemInDarkTheme(), Color400, Color700),
		errorTextColor = MaterialTheme.colorScheme.error,
		errorContainerColor = MaterialTheme.colorScheme.error
	),
	textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
	TextField(
		modifier = modifier,
		value = if (isUpper) value.uppercase(Locale.ROOT) else value,
		onValueChange = onValueChange,
		trailingIcon = trailingIcon,
		readOnly = true, maxLines = 1,
		colors = colores, textStyle = textStyle
	)
}