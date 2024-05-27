package com.batsworks.budget.components.notification

import android.content.Context
import android.widget.Toast

fun CustomToast(context: Context, text: String) {
	Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun CustomLongToast(context: Context, text: String) {
	Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}