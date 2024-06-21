package com.batsworks.budget.ui.theme

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
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
fun SwitchElementsView(
    start: Boolean = true,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
    alternativeContent: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedContent(targetState = start, label = "",
        content = { isVisible ->
            if (isVisible) content()
            else alternativeContent()
        }, transitionSpec = {
            fadeIn(animationSpec = tween(1500)) + scaleIn(animationSpec = tween(2000)) togetherWith fadeOut(
                animationSpec = tween(3000)
            )
        }
    )
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
                .background(Color300.copy(0.6f)),
            enableMergePaths = true, reverseOnRepeat = true,
            iterations = LottieConstants.IterateForever,
            composition = composition, speed = 0.5f,
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
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            keyPath = arrayOf("bg Outlines", "escalador papel", "Group 1", "ADBE Vector Group"),
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color300.toArgb(), BlendModeCompat.SRC_ATOP
            ),
        )
    )

    if (show) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor.copy(0.4f)),
            enableMergePaths = true, reverseOnRepeat = true,
            iterations = LottieConstants.IterateForever,
            composition = composition, speed = speed,
            dynamicProperties = dynamicProperties
        )
    }
}