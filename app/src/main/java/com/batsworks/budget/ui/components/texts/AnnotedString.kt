package com.batsworks.budget.ui.components.texts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.batsworks.budget.ui.theme.textColor

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