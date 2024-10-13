package com.batsworks.budget.components.texts

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
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