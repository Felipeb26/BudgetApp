package com.batsworks.budget.components.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun SwitchElementsView(
	start: Boolean = true,
	time: Int = 1500,
	content: @Composable AnimatedVisibilityScope.() -> Unit,
	alternativeContent: @Composable AnimatedVisibilityScope.() -> Unit,
) {
	AnimatedContent(targetState = start, label = "",
		content = { isVisible ->
			if (isVisible) content()
			else alternativeContent()
		}, transitionSpec = {
			fadeIn(animationSpec = tween(time)) + scaleIn(animationSpec = tween(time + 500)) togetherWith fadeOut(
				animationSpec = tween((time * 1.5).toInt())
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