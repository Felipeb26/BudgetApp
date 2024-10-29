package com.batsworks.budget.ui.components.colum

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.batsworks.budget.R
import com.batsworks.budget.components.animations.infiniteBlink
import com.batsworks.budget.ui.components.buttons.CustomButton
import com.batsworks.budget.ui.components.texts.CustomText
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Padding
import com.batsworks.budget.ui.theme.customBackground
import kotlinx.coroutines.delay

@Composable
fun <T> SwipeToDeleteContainer(
	item: T,
	onDelete: (T) -> Unit,
	animationDuration: Int = 3500,
	content: @Composable (T) -> Unit,
) {
	val (isRemoved, setToRemove) = remember { mutableStateOf(false) }
	val (wasCancelled, setCancelled) = remember { mutableStateOf(false) }
	val state = rememberSwipeToDismissBoxState()

	// Observe state to cancel deletion if needed
	LaunchedEffect(wasCancelled) {
		if (wasCancelled) {
			state.reset()
			setCancelled(false)
		}
	}

	// Observe the swipe state to start deletion countdown
	LaunchedEffect(state.currentValue) {
		if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
			delay(animationDuration.toLong())
			if (!wasCancelled) {
				setToRemove(true)
				onDelete(item)
			}
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
					SwipeToDismissBoxValue.EndToStart -> DeleteBackground(setCancelled)
					SwipeToDismissBoxValue.Settled -> {}
				}
			}) {
			content(item)
		}
	}
}

@Composable
private fun DeleteBackground(setCancelled: (Boolean) -> Unit) {
	Box(
		Modifier
			.fillMaxWidth()
			.background(Color.Red.copy(0.5f))
			.height(80.dp)
			.padding(15.dp),
		contentAlignment = Alignment.Center
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically
		) {
			CustomText(
				textStyle = MaterialTheme.typography.titleSmall.copy(
					letterSpacing = TextUnit(
						0.3f,
						TextUnitType.Sp
					)
				),
				textWeight = FontWeight.Bold,
				text = stringResource(id = R.string.deleting_now)
			)
			CustomButton(
				modifier = Modifier
					.padding(0.dp)
					.height(40.dp)
					.padding(horizontal = 10.dp),
				text = "cancel", textColor = Color50,
				containerColor = infiniteBlink(initialColor = Color500), enable = true,
				onClick = { setCancelled.invoke(true) }
			)
		}
	}
}

@PreviewLightDark
@Composable
fun PreviewDeleteContainer() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(0.dp)
			.background(customBackground)
			.padding(horizontal = Padding.SMALL),
		verticalArrangement = Arrangement.spacedBy(10.dp)
	) {
		DeleteBackground {}
	}
}