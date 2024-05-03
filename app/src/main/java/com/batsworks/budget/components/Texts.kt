package com.batsworks.budget.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import com.batsworks.budget.ui.theme.textColor

@Composable
fun CustomText(
	modifier: Modifier = Modifier,
	text: String,
	textAlign: TextAlign = TextAlign.Start,
	textDecoration: TextDecoration = TextDecoration.None,
	textStyle: TextStyle = MaterialTheme.typography.labelLarge,
	capitalize: Boolean = false,
	color: Color = textColor,
	textWeight: FontWeight = FontWeight.Normal
) {
	Text(
		modifier = modifier,
		text = if (capitalize) capitalizeString(text) else text,
		color = color,
		textAlign = textAlign,
		textDecoration = textDecoration,
		style = textStyle,
		fontWeight = textWeight
	)
}


private fun capitalizeString(text: String): String {
	val firstChar = text.substring(0, 1)
	val restOfChars = text.substring(1)
	return firstChar.toUpperCase(Locale.current).plus(restOfChars)
}
