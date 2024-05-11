package com.batsworks.budget.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import com.batsworks.budget.ui.theme.textColor

@Composable
fun CustomText(
	modifier: Modifier = Modifier,
	text: String,
	textAlign: TextAlign = TextAlign.Start,
	textDecoration: TextDecoration = TextDecoration.None,
	textStyle: TextStyle = MaterialTheme.typography.labelMedium,
	capitalize: Boolean = false,
	color: Color = textColor,
	textWeight: FontWeight = FontWeight.Normal,
	isUpperCase: Boolean = false,
) {
	val textTyped = remember { mutableStateOf(text) }
	if (capitalize) {
		textTyped.value = capitalizeString(textTyped.value)
	}
	if (isUpperCase) {
		textTyped.value = textTyped.value.toUpperCase(Locale.current)
	}

	Text(
		modifier = modifier,
		text = textTyped.value,
		color = color,
		textAlign = textAlign,
		textDecoration = textDecoration,
		style = textStyle,
		fontWeight = textWeight,
		softWrap = false
	)
}


fun capitalizeString(text: String?): String {
	if (text.isNullOrEmpty()) return ""
	val firstChar = text.substring(0, 1)
	val restOfChars = text.substring(1)
	return firstChar.toUpperCase(Locale.current).plus(restOfChars)
}

@Composable
fun annotedString(text1: String, text2: String, vararg values: String): AnnotatedString {
	return buildAnnotatedString {
		withStyle(SpanStyle(color = textColor)) {
			append(text1)
		}
		append(" ")
		values.forEachIndexed { index, value ->
			withStyle(SpanStyle(color = textColor)) {
				if (index == 1) append(text2) else append(" ")
			}
			withStyle(SpanStyle(color = Color.Red.copy(0.7f))) {
				pushStringAnnotation(tag = value, value)
				append(value)
			}
		}
	}
}