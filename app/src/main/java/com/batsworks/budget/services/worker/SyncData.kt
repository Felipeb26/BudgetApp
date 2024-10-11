package com.batsworks.budget.services.worker

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.services.notification.NotificationChannelId
import com.batsworks.budget.services.notification.Notifications
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.batsworks.budget.R
@HiltWorker
class SyncData @AssistedInject constructor(
	@Assisted val context: Context,
	@Assisted params: WorkerParameters,
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
		return try {
			var currentProgress = 0
			val steps = dataSync.size
			val builder = NotificationCompat.Builder(context, NotificationChannelId.CHANNEL.id)
				.setSmallIcon(R.drawable.logo)
				.setContentTitle("Data Synchronization Started")
				.setContentText("Your data synchronization has started.")
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setAutoCancel(true).setOngoing(true)
				.setProgress(100, 0, false)
			notification.showLoadingNotification(builder)


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
				notification.showLoadingNotification(builder)
			}

			builder.setContentText("Sync process finished. All data is up to date.")
				.setOngoing(false)
				.setProgress(0, 0, false)
			notification.showLoadingNotification(builder)
			time += 1
			Result.success()
		} catch (e: Exception) {
			Log.d(TAG, e.message ?: "an error happer")
			notification.showBasicNotification("A error has happen during the syncing")
			Result.failure()
		}
	}

}