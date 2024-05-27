package com.batsworks.budget.components.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.batsworks.budget.R
import kotlin.random.Random

class Notifications(private val context: Context) {

	private val notificationManager = context.getSystemService(NotificationManager::class.java)

	fun showBasicNotification(title: String? = null, text: String? = null) {
		val notification = NotificationCompat.Builder(context, NotificationChannelId.CHANNEL.id)
			.setContentTitle(title ?: "content title")
			.setContentText(text ?: "")
			.setSmallIcon(R.drawable.profile)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setAutoCancel(true)
			.build()

		notificationManager.notify(Random.nextInt(), notification)
	}
}
