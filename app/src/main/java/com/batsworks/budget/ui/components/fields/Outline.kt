package com.batsworks.budget.ui.components.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.batsworks.budget.R
import com.batsworks.budget.ui.components.texts.capitalizeStrings
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.textColor

@Composable
fun CustomOutlineTextField(
	modifier: Modifier = Modifier,
	text: String = "",
	enabled: Boolean = true,
	onValueChange: (String) -> Unit,
	passwordField: Boolean = false,
	labelText: String = "",
	errorMessage: String? = null,
	error: Boolean = false,
	trailingIcon: ImageVector? = null,
	leadingIcon: ImageVector? = null,
	shape: Shape = RoundedCornerShape(30),
	keyboardType: KeyboardType = KeyboardType.Text,
	onDone: (() -> Unit)? = null,
	transformation: VisualTransformation = VisualTransformation.None,
) {
	val focusManager = LocalFocusManager.current
	val (show, setShow) = remember { mutableStateOf(passwordField) }

	OutlinedTextField(
		keyboardActions = KeyboardActions(onDone = {
			focusManager.clearFocus()
			onDone?.invoke()
		}),
		keyboardOptions = KeyboardOptions(
			imeAction = ImeAction.Done,
			keyboardType = if (passwordField) KeyboardType.NumberPassword else keyboardType
		),
		modifier = modifier,
		value = text,
		onValueChange = onValueChange,
		label = { Text(text = capitalizeStrings(labelText)) },
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
			focusedBorderColor = Color300,
			focusedLabelColor = Color300,
			unfocusedBorderColor = Color300.copy(0.7f),
			errorSupportingTextColor = MaterialTheme.colorScheme.error,
		),
		visualTransformation = if (passwordField && show) PasswordVisualTransformation() else transformation,
		shape = shape,
		leadingIcon = leadingIcon?.let {
			{
				Icon(
					imageVector = it,
					contentDescription = "",
					tint = if (enabled) textColor else textColor.copy(0.4f)
				)
			}
		},
		trailingIcon = trailingIcon?.let {
			{
				Icon(
					modifier = if (enabled) Modifier.clickable { if (passwordField) setShow(!show) } else Modifier,
					imageVector = it,
					contentDescription = "",
					tint = if (enabled) textColor else textColor.copy(0.4f)
				)
			}
		}
	)
}

@Composable
fun CustomPasswordOutlineTextField(
	modifier: Modifier = Modifier,
	text: String = "",
	textStyle: TextStyle = LocalTextStyle.current,
	enabled: Boolean = true,
	onValueChange: (String) -> Unit,
	passwordField: Boolean = false,
	labelText: String = "",
	errorMessage: String? = null,
	error: Boolean = false,
	shape: Shape = RoundedCornerShape(30),
	keyboardType: KeyboardType = KeyboardType.Text,
	onDone: (() -> Unit)? = null,
	transformation: VisualTransformation = VisualTransformation.None,
) {
	val focusManager = LocalFocusManager.current
	val (show, setShow) = remember { mutableStateOf(passwordField) }

	val trailingIcon = visible(show)

	OutlinedTextField(
		keyboardActions = KeyboardActions(onDone = {
			focusManager.clearFocus()
			onDone?.invoke()
		}),
		keyboardOptions = KeyboardOptions(
			imeAction = ImeAction.Done,
			keyboardType = if (passwordField) KeyboardType.NumberPassword else keyboardType
		),
		modifier = modifier,
		value = text,
		onValueChange = onValueChange,
		label = {
			Text(
				text = capitalizeStrings(labelText),
				color = textColor.copy(0.4f),
				style = textStyle
			)
		},
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
			focusedBorderColor = Color300,
			focusedLabelColor = Color300,
			errorSupportingTextColor = MaterialTheme.colorScheme.error,
		),
		visualTransformation = if (passwordField && show) PasswordVisualTransformation() else transformation,
		shape = shape,
		trailingIcon = trailingIcon.let {
			{
				Icon(
					modifier = if (enabled) Modifier.clickable {
						if (passwordField) setShow(!show)
					} else Modifier,
					imageVector = it,
					contentDescription = "",
					tint = if (enabled) textColor else textColor.copy(0.4f)
				)
			}
		}
	)
}

@Composable
private fun visible(show: Boolean): ImageVector {
	return if (show) ImageVector.vectorResource(R.drawable.ic_visibility_off)
	else ImageVector.vectorResource(R.drawable.ic_visibility)
}