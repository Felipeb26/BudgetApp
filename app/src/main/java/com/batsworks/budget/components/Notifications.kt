package com.batsworks.budget.components

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.AbstractCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CustomToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun CustomLongToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun CustomSnackBar(
    coroutine: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String
) {
    coroutine.launch {
        snackBarHostState.showSnackbar("", withDismissAction = true)
    }
}