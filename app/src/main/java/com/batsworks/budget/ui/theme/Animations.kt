package com.batsworks.budget.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color

@Composable
fun AnimateIcon(
    makeShine: Boolean,
    setMakeShine: (Boolean) -> Unit,
    color: Animatable<Color, AnimationVector4D>
) {
    LaunchedEffect(makeShine) {
        if (!makeShine) {
            color.animateTo(color.value.copy(1f), animationSpec = tween(2000))
        } else {
            color.animateTo(color.value.copy(0.6f), animationSpec = tween(2000))
        }
        setMakeShine(!makeShine)
    }
}

@Composable
fun AnimateIcon(
    makeShine: Boolean,
    setMakeShine: (Boolean) -> Unit,
    color: Animatable<Color, AnimationVector4D>,
    valueMinimun: Float
) {
    LaunchedEffect(makeShine) {
        if (!makeShine) {
            color.animateTo(color.value.copy(1f), animationSpec = tween(2000))
        } else {
            color.animateTo(color.value.copy(valueMinimun), animationSpec = tween(2000))
        }
        setMakeShine(!makeShine)
    }
}