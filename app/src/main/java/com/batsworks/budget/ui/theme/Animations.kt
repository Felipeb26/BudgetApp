package com.batsworks.budget.ui.theme

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.batsworks.budget.R

@Composable
fun InfiniteBlink(
	initialColor: Color,
	finalColor: Color = initialColor.copy(0.5f),
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

@Composable
fun Loading(
	isLoading: Boolean,
) {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
	if (isLoading) {
		LottieAnimation(
			modifier = Modifier
				.fillMaxSize()
				.background(Color800.copy(0.6f)),
			enableMergePaths = true,
			iterations = LottieConstants.IterateForever,
			composition = composition,
			speed = 0.5f,
		)
	}
}


@Composable
fun CustomLottieAnimation(
	lottieComposition: Int,
	show: Boolean = false,
	speed: Float = 0.5f,
	backgroundColor: Color = Color800.copy(0.6f),
) {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieComposition))
	if (show) {
		LottieAnimation(
			modifier = Modifier
				.fillMaxSize()
				.background(backgroundColor),
			enableMergePaths = true,
			iterations = LottieConstants.IterateForever,
			composition = composition,
			speed = speed
		)
	}
}