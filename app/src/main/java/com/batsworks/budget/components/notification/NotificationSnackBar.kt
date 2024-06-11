package com.batsworks.budget.components.notification

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CustomSnackBar(
	coroutine: CoroutineScope,
	snackBarHostState: SnackbarHostState,
	message: String,
) {
	coroutine.launch {
		snackBarHostState.showSnackbar(
			message, withDismissAction = true,
			duration = SnackbarDuration.Short
		)
	}
}

suspend fun CustomSuspendSnackBar(snackBarHostState: SnackbarHostState, message: String) {
	snackBarHostState.showSnackbar(message, withDismissAction = true)
}