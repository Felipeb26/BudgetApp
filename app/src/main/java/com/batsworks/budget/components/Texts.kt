package com.batsworks.budget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.batsworks.budget.R
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.theme.textColor

@Composable
fun CustomText(
	modifier: Modifier = Modifier,
	textStyle: TextStyle = MaterialTheme.typography.labelMedium,
	textDecoration: TextDecoration = TextDecoration.None,
	textWeight: FontWeight = FontWeight.Normal,
	textAlign: TextAlign = TextAlign.Start,
	color: Color = textColor,
	isUpperCase: Boolean,
	text: String,
	iconBitMap: ImageVector,
	clickEvent: (() -> Unit),
) {
	Row(
		modifier = Modifier.clickable { clickEvent.invoke() },
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			modifier = modifier,
			text = if (isUpperCase) text.toUpperCase(Locale.current) else text,
			color = color,
			textAlign = textAlign,
			textDecoration = textDecoration,
			style = textStyle,
			fontWeight = textWeight,
			softWrap = false
		)
		Spacer(modifier = modifier.width(5.dp))
		Icon(imageVector = iconBitMap, contentDescription = "", tint = color)
	}
}

@Composable
fun CustomText(
	modifier: Modifier = Modifier,
	text: String,
	textAlign: TextAlign = TextAlign.Start,
	textDecoration: TextDecoration = TextDecoration.None,
	textStyle: TextStyle = MaterialTheme.typography.labelMedium,
	color: Color = textColor,
	textWeight: FontWeight = FontWeight.Normal,
) {
	Text(
		modifier = modifier,
		text = text,
		color = color,
		textAlign = textAlign,
		textDecoration = textDecoration,
		style = textStyle,
		fontWeight = textWeight,
		softWrap = text.length >= 35
	)
}

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
	wrap: Boolean = false,
	space: TextUnit = TextUnit.Unspecified,
) {
	var textTyped = text
	if (capitalize) textTyped = capitalizeStrings(textTyped)
	if (isUpperCase) textTyped = textTyped.toUpperCase(Locale.current)

	Text(
		modifier = modifier,
		text = textTyped,
		color = color,
		textAlign = textAlign,
		textDecoration = textDecoration,
		style = textStyle,
		fontWeight = textWeight,
		softWrap = wrap,
		letterSpacing = space
	)
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
				if (index == 1) append("\t$text2\t") else append(" ")
			}

			withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
				pushStringAnnotation(tag = value, value)
				append(value)
			}
		}
	}
}

fun capitalizeStrings(text: String?): String {
	if (text.isNullOrEmpty()) return ""
	val values = text.split(" ").map { capitalize(it.trim()) }.toList()
	return values.joinToString(" ")
}

private fun capitalize(text: String?): String {
	if (text.isNullOrEmpty()) return ""
	val firstChar = text.substring(0, 1)
	val restOfChars = text.substring(1)
	return firstChar.toUpperCase(Locale.current).plus(restOfChars)
}

fun AJUST_TAG(tag: String): String {
	return tag.substring(tag.lastIndexOf(".") + 1)
}

@Composable
fun formatScreenTitle(screen: Screen?): String {
	return when (screen) {
		Screen.AccountsScreen -> stringResource(id = R.string.account)
		Screen.AdicionarScreen -> stringResource(id = R.string.add)
		Screen.HistoryScreen -> stringResource(id = R.string.history)
		Screen.HomeScreen -> stringResource(id = R.string.home)
		Screen.PlusScreen -> stringResource(id = R.string.plus)
		Screen.ProfileScreen -> stringResource(id = R.string.profile)
		Screen.SettingScreen -> stringResource(id = R.string.settings)
		else -> stringResource(id = R.string.exit)
	}.toUpperCase(Locale.current)
}

fun formatScreenTitle(title: String): String {
	return title.replace("_screen", "").toUpperCase(Locale.current)
}
