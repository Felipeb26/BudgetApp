package com.batsworks.budget.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


@Composable
fun brushIcon(color: Color = Color400): Brush {
    return Brush.verticalGradient(
        listOf(
            color.copy(0.2f),
            color.copy(0.6f),
            color.copy(0.4f)
        )
    )
}

fun brushIcon(isDark: Boolean): Brush {
    val color = if (isDark) Color950 else Color200
    return Brush.verticalGradient(
        listOf(
            color.copy(0.2f),
            color.copy(0.8f),
            color.copy(0.4f)
        )
    )
}

fun brushCard(
    isVerticalGratient: Boolean = true,
    colors: List<Color> = listOf(Color400, Color300, Color900),
    cardStart: Float = 0.0f,
    cardEnd: Float = 150f,
): Brush {

    if (isVerticalGratient) {
        return Brush.verticalGradient(
            0.0f to colors[0].copy(0.6f),
            0.5f to colors[1],
            1.0f to colors[2],
            startY = cardStart,
            endY = cardEnd
        )
    } else {
        return Brush.horizontalGradient(
            0.0f to colors[0].copy(0.8f),
            0.5f to colors[1],
            1.0f to colors[2],
            startX = cardStart,
            endX = cardEnd
        )
    }
}