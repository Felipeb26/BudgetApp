package com.batsworks.budget.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.batsworks.budget.R
import com.batsworks.budget.ui.theme.Color50
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
	item: T,
	onDelete: (T) -> Unit,
	animationDuration: Int = 1500,
	content: @Composable (T) -> Unit,
) {
	var isRemoved by remember { mutableStateOf(false) }
	val state = rememberSwipeToDismissBoxState()

	LaunchedEffect(state.currentValue) {
		if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
			delay(animationDuration.toLong())
			onDelete(item)
			isRemoved = !isRemoved
		}
	}

	AnimatedVisibility(
		visible = !isRemoved,
		exit = shrinkVertically(
			animationSpec = tween(animationDuration),
			shrinkTowards = Alignment.Top
		)
	) {
		SwipeToDismissBox(state = state,
			enableDismissFromStartToEnd = false,
			backgroundContent = {
				when (state.dismissDirection) {
					SwipeToDismissBoxValue.StartToEnd -> {}
					SwipeToDismissBoxValue.EndToStart -> DeleteBackground()
					SwipeToDismissBoxValue.Settled -> {}
				}
			}) {
			content(item)
		}
	}
}

@Composable
private fun DeleteBackground() {
	val color = if (isSystemInDarkTheme()) Color.Red.copy(0.4f) else Color.Red

	Box(
		Modifier
			.fillMaxSize()
			.background(color)
			.height(30.dp)
			.padding(15.dp),
		contentAlignment = Alignment.CenterEnd
	) {
		Icon(
			modifier = Modifier
				.padding(horizontal = 20.dp)
				.height(30.dp),
			imageVector = Icons.Default.Delete,
			contentDescription = "",
			tint = Color50
		)
		CustomText(
			modifier = Modifier.padding(horizontal = 50.dp),
			text = stringResource(id = R.string.deleting_now)
		)
	}
}