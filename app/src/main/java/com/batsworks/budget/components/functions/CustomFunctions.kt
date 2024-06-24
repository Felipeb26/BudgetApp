package com.batsworks.budget.components.functions

import android.content.Context
import com.batsworks.budget.components.notification.NotificationToast

fun <T> notEnableIfEmpty(context: Context, message: String, content: T?, func: () -> Unit) {
    val toast = NotificationToast(context)
    var isNullOrEmpty = false
    if (content == null) isNullOrEmpty = true
    if (content is List<*>) isNullOrEmpty = content.isEmpty()
    if (content is Array<*>) isNullOrEmpty = content.isEmpty()
    if (content is HashMap<*, *>) isNullOrEmpty = content.isEmpty()

    if (!isNullOrEmpty) {
        func.invoke()
    } else toast.show(message)
}

fun <T> composeBool(item: Boolean, ifTrue: T, ifFalse: T): T {
    return if (item) ifTrue else ifFalse
}

fun assertValues(isTrue: String?, isFalse: String): String {
    if (isTrue == null) return isFalse
    if (isTrue.isEmpty()) return isFalse
    if (isTrue.isBlank()) return isFalse
    return isTrue
}