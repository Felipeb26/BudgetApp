package com.batsworks.budget.components.notification

import android.content.Context
import android.widget.Toast
import com.batsworks.budget.R

fun CustomToast(context: Context, text: String) {
	Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun CustomLongToast(context: Context, text: String) {
	Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

class NotificationToast(val context: Context) {

	private val notImplemented = context.getString(R.string.not_yet_implemented)

	fun customToast(text: String? = null) {
		val value = if (text.isNullOrEmpty()) notImplemented else text
		Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
	}

	fun customLongToast(text: String? = null) {
		val value = if (text.isNullOrEmpty()) notImplemented else text
		Toast.makeText(context, value, Toast.LENGTH_LONG).show()
	}
}