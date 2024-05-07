package com.batsworks.budget.ui.theme

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun InfiniteBlink(
	initialColor: Color,
	finalColor: Color = initialColor.copy(1f),
	transitionTime: Int = 2000,
): Color {
	val transition = rememberInfiniteTransition(label = "infinite blink")
	val animatedColor by transition.animateColor(
		initialValue = initialColor,
		targetValue = finalColor,
		animationSpec = infiniteRepeatable(
			animation = tween(transitionTime),
			repeatMode = RepeatMode.Reverse,
		), label = ""
	)
	return animatedColor
}