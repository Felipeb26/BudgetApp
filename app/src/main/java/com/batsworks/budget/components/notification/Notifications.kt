package com.batsworks.budget.components.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.batsworks.budget.R
import kotlin.random.Random

class Notifications(private val context: Context) {

	private val notificationManager = context.getSystemService(NotificationManager::class.java)

	fun showBasicNotification(text: String? = null) {
		val notification = NotificationCompat.Builder(context, NotificationChannelId.CHANNEL.id)
			.setContentTitle("BatsWorks")
			.setContentText(text ?: "hfvbljzvfdkjvdd")
			.setSmallIcon(R.drawable.profile)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setAutoCancel(true)
			.build()

		notificationManager.notify(Random.nextInt(), notification)
	}

	fun showBasicNotification(text: String? = null, filePath: String) {
		val intent = Intent(context, Notifications::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			putExtra("file_path", filePath)
		}
		val pendingIntent: PendingIntent = PendingIntent.getActivity(
			context, 0, intent,
			PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		)

		val notification = NotificationCompat.Builder(context, NotificationChannelId.CHANNEL.id)
			.setContentTitle("BatsWorks")
			.setContentText(text ?: "")
			.setSmallIcon(R.drawable.profile)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.build()

		notificationManager.notify(Random.nextInt(), notification)
	}
}
