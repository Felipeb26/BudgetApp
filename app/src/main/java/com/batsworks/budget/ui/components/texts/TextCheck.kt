package com.batsworks.budget.ui.components.texts

import android.content.Context
import com.batsworks.budget.utils.common.UiText

fun <T> checkString(value: T, context: Context? = null): String {
    if (context == null) return value.toString()
    return try {
        when (value) {
            is Int -> UiText.StringResource(value).asString(context)
            is String -> value
            else -> value.toString()
        }
    } catch (e: Exception) {
        value.toString()
    }
}

fun <T> checkString(value: String?, alternative: T): String {
    require(alternative != null)
    return if (value.isNullOrEmpty()) alternative.toString() else value
}