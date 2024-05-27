package com.batsworks.budget.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Color50 = Color(0xFFf3f2fb)
val Color100 = Color(0xFFeae8f7)
val Color200 = Color(0xFFd8d5f0)
val Color300 = Color(0xFFc0bae7)
val Color400 = Color(0xFFaa9edb)
val Color500 = Color(0xFF9a86ce)
val Color600 = Color(0xFF8b6dbe)
val Color700 = Color(0xFF785ca6)
val Color800 = Color(0xFF5c487f)
val Color900 = Color(0xFF2f273f)
val Color950 = Color(0xFF51426d)
val ColorDark = Color(0xFF141404)

val customDarkBackground
    @Composable
    get() = if (isSystemInDarkTheme()) ColorDark else Color.White
val customBackground
    @Composable
    get() = if (isSystemInDarkTheme()) Color800.copy(alpha = 0.7f) else Color.White
val ColorScheme.outOfFocusText
    @Composable
    get() = if(isSystemInDarkTheme()) Color100 else Color50

val textColor
    @Composable
    get() = if(isSystemInDarkTheme()) Color50 else Color950

fun GradientBrush(
    isVerticalGratient: Boolean = true,
    colors: List<Color> = listOf(Color800, Color700, Color400),
): Brush {

    val endOffset = if (isVerticalGratient)
        Offset(0f, Float.POSITIVE_INFINITY)
    else Offset(Float.POSITIVE_INFINITY, 0f)

    return Brush.linearGradient(colors, start = Offset.Zero, endOffset)
}