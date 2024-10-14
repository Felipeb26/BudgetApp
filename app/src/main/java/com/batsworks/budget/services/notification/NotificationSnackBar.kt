package com.batsworks.budget.services.notification

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class NotificationSnackBar(
    private val coroutine: CoroutineScope,
    private val snackBarHostState: SnackbarHostState
) {

    fun show(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        dismissAction: Boolean = true
    ) {
        coroutine.launch {
            snackBarHostState.showSnackbar(
                message, actionLabel,
                withDismissAction = dismissAction,
                duration = duration,
            )
        }
    }

}