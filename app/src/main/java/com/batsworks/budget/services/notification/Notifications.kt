package com.batsworks.budget.services.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.batsworks.budget.MainActivity
import com.batsworks.budget.R
import kotlin.random.Random

class Notifications(private val context: Context) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val title = context.getString(R.string.enterprise_name)

    fun showBasicNotification(text: String? = null) {
        val notificationBuilder =
            NotificationCompat.Builder(context, NotificationChannelId.CHANNEL.id)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.profile)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        val notification = if (text.isNullOrEmpty()) {
            notificationBuilder.build()
        } else {
            notificationBuilder.setContentText(text).build()
        }

        notificationManager.notify(Random.nextInt(), notification)
    }

    fun showPendingNotification(text: String? = null, filePath: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("file_path", filePath)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder =
            NotificationCompat.Builder(context, NotificationChannelId.CHANNEL.id)
                .setContentTitle(title)
                .setContentText(text ?: "")
                .setSmallIcon(R.drawable.profile)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val notification = if (text.isNullOrEmpty()) {
            notificationBuilder.build()
        } else {
            notificationBuilder.setContentText(text).build()
        }

        notificationManager.notify(Random.nextInt(), notification)
    }

    fun showLoadingNotification(context: Context, title: String? = null) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

        val builder = NotificationCompat.Builder(context, NotificationChannelId.CHANNEL.id)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title ?: "Download Progress")
            .setContentText("Your data is syncroning now")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setProgress(100, 100, true).build()

        notificationManager.notify(1, builder)
    }
}
