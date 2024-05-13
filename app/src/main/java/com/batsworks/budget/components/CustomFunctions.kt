package com.batsworks.budget.components

import android.content.Context

fun <T> notEnableIfEmpty(context: Context, message: String, list: T?, func: () -> Unit) {
	var isNullOrEmpty = false
	if (list == null) isNullOrEmpty = true
	if (list is List<*>) isNullOrEmpty = list.isEmpty()
	if (!isNullOrEmpty) {
		func.invoke()
	} else {
		CustomToast(context, message)
	}
}