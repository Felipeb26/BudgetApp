package com.batsworks.budget

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.room.Room
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.batsworks.budget.components.AJUST_TAG
import com.batsworks.budget.domain.dao.Database
import com.batsworks.budget.services.notification.NotificationChannelId
import com.batsworks.budget.services.worker.SyncData
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class BudgetApplication : Application(), ImageLoaderFactory {

	private val tag = BudgetApplication::class.java.name

	override fun newImageLoader(): ImageLoader {
		return ImageLoader(this).newBuilder()
			.memoryCachePolicy(CachePolicy.ENABLED)
			.memoryCache {
				MemoryCache.Builder(this)
					.maxSizePercent(0.1)
					.strongReferencesEnabled(true)
					.build()
			}
			.diskCachePolicy(CachePolicy.ENABLED)
			.diskCache {
				DiskCache.Builder()
					.maxSizePercent(0.03)
					.directory(cacheDir)
					.build()
			}.logger(DebugLogger())
			.build()
	}

	companion object {
		lateinit var database: Database
	}

	override fun onCreate() {
		super.onCreate()
		database = Room.databaseBuilder(
			applicationContext,
			Database::class.java,
			Database.NAME
		).setQueryCallback({ query, args ->
			Log.d("QUERY", query)
			if (args.isNotEmpty()) Log.d("ARGS", "$args")
		}, Executors.newSingleThreadExecutor())
			.fallbackToDestructiveMigration()
			.build()

		val notificationChannel = NotificationChannel(
			NotificationChannelId.CHANNEL.id,
			"Notification BatsWorks Budget",
			NotificationManager.IMPORTANCE_HIGH
		)

		notificationChannel.description = "A notification from you budget app"
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(notificationChannel)

		syncData()
	}

	var int = 0
	private fun syncData() {
		int = int.inc()
		Log.d("STARTED", "$int times")
		val FINAL_TAG = AJUST_TAG(tag)

		val contraints = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.build()

		val workerRequest = PeriodicWorkRequestBuilder<SyncData>(
			repeatInterval = 1,
			repeatIntervalTimeUnit = TimeUnit.MINUTES,
			flexTimeInterval = 10,
			flexTimeIntervalUnit = TimeUnit.SECONDS
		).setBackoffCriteria(
			backoffPolicy = BackoffPolicy.LINEAR,
			duration = Duration.ofSeconds(15)
		).setConstraints(contraints).addTag(FINAL_TAG).build()

		val workManager = WorkManager.getInstance(this)
		workManager.cancelAllWork()

		workManager.enqueueUniquePeriodicWork(FINAL_TAG, ExistingPeriodicWorkPolicy.UPDATE, workerRequest)
		workManager.getWorkInfosForUniqueWorkLiveData(FINAL_TAG)
			.observeForever {
				it.forEach { workInfo ->
					Log.d("DATA_SYNC", "${workInfo.state}")
				}
			}

	}

}