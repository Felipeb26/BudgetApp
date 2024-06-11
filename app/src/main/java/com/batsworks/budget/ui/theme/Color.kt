package com.batsworks.budget.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
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
	get() = if (isSystemInDarkTheme()) Color600.copy(0.4f) else Color.White.copy(0.6f)
val customBackground
	@Composable
	get() = if (isSystemInDarkTheme()) Color800.copy(alpha = 0.7f) else Color.White
val ColorScheme.outOfFocusText
	@Composable
	get() = if (isSystemInDarkTheme()) Color100 else Color50

val textColor
	@Composable
	get() = if (isSystemInDarkTheme()) Color50 else Color950

fun brushIcon(color: Color = Color950): Brush {
	return Brush.verticalGradient(
		listOf(
			color.copy(0.2f),
			color.copy(0.6f),
			color.copy(0.4f)
		)
	)
}
fun brushIcon(isDark: Boolean): Brush {
	val color = if(isDark) Color950 else Color200
	return Brush.verticalGradient(
		listOf(
			color.copy(0.2f),
			color.copy(0.6f),
			color.copy(0.4f)
		)
	)
}

fun GradientBrush(
	isVerticalGratient: Boolean = true,
	colors: List<Color> = listOf(Color800, Color700, Color400),
): Brush {

	val endOffset = if (isVerticalGratient)
		Offset(0f, Float.POSITIVE_INFINITY)
	else Offset(Float.POSITIVE_INFINITY, 0f)

	return Brush.linearGradient(colors, start = Offset.Zero, endOffset)
}