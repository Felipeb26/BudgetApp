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
    private val defaultTitle = context.getString(R.string.enterprise_name)

    fun showBasicNotification(
        title: String = defaultTitle,
        text: String? = null,
        autoCancelAndOngoing: Pair<Boolean, Boolean> = Pair(true, false)
    ) {
        val notificationBuilder =
            NotificationCompat.Builder(context, NotificationDataCreation.CHANNEL.content)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.profile)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(autoCancelAndOngoing.first).setOngoing(autoCancelAndOngoing.second)

        val notification = if (text.isNullOrEmpty()) {
            notificationBuilder.build()
        } else {
            notificationBuilder.setContentText(text).build()
        }

        notificationManager.notify(Random.nextInt(), notification)
    }

    fun showBasicNotification(
        title: String = defaultTitle,
        text: String? = null,
        icon: Int,
        actionLabel: String,
        intent: PendingIntent,
        autoCancelAndOngoing: Pair<Boolean, Boolean> = Pair(true, false)
    ) {
        val notificationBuilder =
            NotificationCompat.Builder(context, NotificationDataCreation.CHANNEL.content)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.profile)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(autoCancelAndOngoing.first).setOngoing(autoCancelAndOngoing.second)
                .addAction(icon, actionLabel, intent)

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
            NotificationCompat.Builder(context, NotificationDataCreation.CHANNEL.content)
                .setContentTitle(defaultTitle)
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

    fun showNotification(builder: NotificationCompat.Builder, id: Int = 1) {
        notificationManager.notify(id, builder.build())
    }
}
