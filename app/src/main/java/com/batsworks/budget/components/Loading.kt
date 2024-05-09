package com.batsworks.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.batsworks.budget.R
import com.batsworks.budget.ui.theme.Color800


@Composable
fun Loading(
	isLoading: Boolean,
) {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
	if (isLoading) {
		LottieAnimation(
			modifier = Modifier
				.fillMaxSize()
				.background(Color800.copy(0.2f)),
			iterations = LottieConstants.IterateForever,
			composition = composition,
			speed = 0.5f
		)
	}
}
