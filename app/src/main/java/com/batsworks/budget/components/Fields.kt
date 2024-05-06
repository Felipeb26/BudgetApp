package com.batsworks.budget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
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
    errorMessage: String = "",
    error: Boolean = false,
    trailingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    shape: Shape = RoundedCornerShape(30)
) {
    val (show, setShow) = remember { mutableStateOf(passwordField) }

    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = if (passwordField) KeyboardType.NumberPassword else keyboardType),
        modifier = modifier,
        value = defaultText,
        onValueChange = onValueChange,
        label = { Text(text = capitalizeString(labelText), color = textColor.copy(0.4f)) },
        supportingText = { Text(text = errorMessage) },
        enabled = enabled,
        singleLine = true,
        isError = error,
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor.copy(0.4f),
            focusedContainerColor = customBackground,
            unfocusedContainerColor = customBackground.copy(0.4f),
        ),
        visualTransformation = if (passwordField && show) PasswordVisualTransformation() else VisualTransformation.None,
        shape = shape,
        trailingIcon = trailingIcon?.let {
            {
                Icon(
                    modifier = Modifier.clickable { if (passwordField) setShow(!show) },
                    imageVector = it,
                    contentDescription = "",
                    tint = textColor
                )
            }
        }
    )
}

private fun capitalizeString(text: String?): String {
    if (text.isNullOrEmpty()) return ""
    val firstChar = text.substring(0, 1);
    val restOfChars = text.substring(1)
    return firstChar.toUpperCase(Locale.current).plus(restOfChars)
}