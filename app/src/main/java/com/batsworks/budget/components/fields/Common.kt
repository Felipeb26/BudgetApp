package com.batsworks.budget.components.fields

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color700


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
        focusedContainerColor = Color700,
        unfocusedContainerColor = Color700,
        errorTextColor = MaterialTheme.colorScheme.error,
        errorContainerColor = MaterialTheme.colorScheme.error
    ),
) {
    TextField(
        modifier = modifier,
        value = if (isUpper) value.toUpperCase(Locale.current) else value,
        onValueChange = onValueChange,
        trailingIcon = trailingIcon,
        readOnly = true, maxLines = 1,
        colors = colores
    )
}