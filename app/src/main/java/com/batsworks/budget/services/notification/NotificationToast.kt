package com.batsworks.budget.services.notification

import android.content.Context
import android.widget.Toast
import com.batsworks.budget.R

class NotificationToast(val context: Context) {

    private val notImplemented = context.getString(R.string.not_yet_implemented)

    fun show(text: String? = null) {
        val value = if (text.isNullOrEmpty()) notImplemented else text
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
    }

    fun show(text: String? = null, duration: Int = Toast.LENGTH_SHORT) {
        val value = if (text.isNullOrEmpty()) notImplemented else text
        Toast.makeText(context, value, duration).show()
    }
}