package com.batsworks.budget.components.fields

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase


@Composable
fun CustomTextField(
	modifier: Modifier = Modifier,
	value: String = "",
	onValueChange: (String) -> Unit,
	trailingIcon: @Composable (() -> Unit)? = null,
	isUpper: Boolean = false,
) {
	TextField(
		modifier = modifier,
		value = if (isUpper) value.toUpperCase(Locale.current) else value,
		onValueChange = onValueChange,
		readOnly = true,
		trailingIcon = trailingIcon,
		maxLines = 1
	)
}