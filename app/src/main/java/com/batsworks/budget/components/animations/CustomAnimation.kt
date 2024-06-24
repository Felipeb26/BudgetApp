package com.batsworks.budget.components.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color


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
    alternativeContent: @Composable AnimatedVisibilityScope.() -> Unit,
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
fun Visible(
    show: Boolean = false,
    enter: EnterTransition = expandVertically(
        animationSpec = tween(500),
        expandFrom = Alignment.Top
    ) + fadeIn(),
    exit: ExitTransition = shrinkVertically(
        animationSpec = tween(1500),
        shrinkTowards = Alignment.Top
    ) + fadeOut(
        animationSpec = tween(1600)
    ),
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(visible = show, enter = enter, exit = exit) {
        content()
    }
}