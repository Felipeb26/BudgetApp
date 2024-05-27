package com.batsworks.budget.components

import android.content.Context
import com.batsworks.budget.components.notification.CustomToast

fun <T> notEnableIfEmpty(context: Context, message: String, content: T?, func: () -> Unit) {
	var isNullOrEmpty = false
	if (content == null) isNullOrEmpty = true
	if (content is List<*>) isNullOrEmpty = content.isEmpty()
	if (content is Array<*>) isNullOrEmpty = content.isEmpty()
	if (content is HashMap<*, *>) isNullOrEmpty = content.isEmpty()
	if (!isNullOrEmpty) {
		func.invoke()
	} else {
		CustomToast(context, message)
	}
}