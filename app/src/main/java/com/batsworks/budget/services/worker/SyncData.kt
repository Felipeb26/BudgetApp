package com.batsworks.budget.services.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.batsworks.budget.R
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.services.notification.CancelWorkerNotification
import com.batsworks.budget.services.notification.NotificationDataCreation
import com.batsworks.budget.services.notification.Notifications
import com.batsworks.budget.services.notification.RetryWorkerNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncData @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    usersDao: UsersDao,
    amountDao: AmountDao,
    amountRepository: CustomRepository<AmountFirebaseEntity>,
) : CoroutineWorker(context, params) {


    companion object {
        lateinit var dataSync: List<DataSyncFactory>
        const val TAG: String = "SYNC_DATA"
        var time = 0
    }

    init {
        dataSync = listOf(
            SyncUserData(),
            SyncAmountData(
                usersDao,
                amountDao,
                amountRepository,
            )
        )
    }

    override suspend fun doWork(): Result {
        val notification = Notifications(context)
        val pendingIntentAction = pedingIntent()
        return try {
            var currentProgress = 0
            val steps = dataSync.size

            val builder = notificationBuilder(
                title = "Data Synchronization Started",
                text = "Your data synchronization has started."
            ).setProgress(100, 0, false)
                .addAction(R.drawable.ic_cancel, "cancel", pendingIntentAction.first)
            notification.showNotification(builder)

            Log.d(TAG, "RODOU $time")

            for (syncItem in dataSync) {
                if (syncItem.needsUpdate()) {
                    syncItem.update()
                }
                if (syncItem.needsBringData()) {
                    syncItem.save()
                }

                currentProgress += (100 / steps)
                builder.setProgress(100, currentProgress, false)
                notification.showNotification(builder)
            }

            builder
                .setContentTitle("Data Synchronization finished")
                .setContentText("Sync process finished. All data is up to date.")
                .setOngoing(false)
                .setProgress(0, 0, false)
            notification.showNotification(builder)
            time += 1
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, e.message ?: "an error happer")
            notification.showBasicNotification(
                title = "Data Synchronization error",
                text = e.message ?: "an error has happen while sycronization",
                R.drawable.ic_refresh, "retry", pendingIntentAction.second
            )
            Result.failure(Data.Builder().putString("error", e.toString()).build())
        }
    }

    private fun pedingIntent(): Pair<PendingIntent, PendingIntent> {
        val intent = Intent(context, CancelWorkerNotification::class.java).apply {
            putExtra("worker_tag", params.id)
        }
        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val retryIntent = Intent(context, RetryWorkerNotification::class.java).apply {
            putExtra("worker_tag", params.id)
        }
        val retryPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            retryIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return Pair(cancelPendingIntent, retryPendingIntent)
    }

    private fun notificationBuilder(
        title: String,
        text: String,
        ongoing: Boolean = false,
        autoCancel: Boolean = true
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NotificationDataCreation.CHANNEL.content)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(ongoing)
            .setAutoCancel(autoCancel)
    }

}