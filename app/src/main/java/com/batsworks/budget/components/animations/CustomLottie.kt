package com.batsworks.budget.components.animations

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
import com.airbnb.lottie.compose.LottieDynamicProperty
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.batsworks.budget.R
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950
import com.batsworks.budget.ui.theme.customBackground

@Composable
fun Loading(isLoading: Boolean = true) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val staticBallList = mutableListOf<LottieDynamicProperty<*>>()

    for(i in 1..8) {
        staticBallList.add(rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            keyPath = arrayOf("Shape Layer $i", "Ellipse 1", "Fill 1"),
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color300.toArgb(), BlendModeCompat.SRC_ATOP
            )))
    }


    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            keyPath = arrayOf("Glow ball", "Ellipse 1", "Fill 1"),
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color600.copy(0.6f).toArgb(), BlendModeCompat.SRC_ATOP
            ),
        ),
        *staticBallList.toTypedArray()
    )

    if (isLoading) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize()
                .background(customBackground),
            enableMergePaths = true, reverseOnRepeat = true,
            iterations = LottieConstants.IterateForever,
            composition = composition, speed = 0.5f,
            dynamicProperties = dynamicProperties
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
            keyPath = arrayOf("Layer 3", "Object", ""),
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color950.toArgb(), BlendModeCompat.SRC_ATOP
            ),
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            keyPath = arrayOf("Layer 2", "Object", ""),
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color500.toArgb(), BlendModeCompat.SRC_ATOP
            ),
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            keyPath = arrayOf("Layer 1", "Object", ""),
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color50.toArgb(), BlendModeCompat.SRC_ATOP
            ),
        )
    )

    if (show) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor.copy(0.7f)),
            enableMergePaths = true, reverseOnRepeat = true,
            iterations = LottieConstants.IterateForever,
            composition = composition, speed = speed,
            dynamicProperties = dynamicProperties
        )
    }
}