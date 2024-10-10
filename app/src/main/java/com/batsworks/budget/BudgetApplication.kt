package com.batsworks.budget

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.batsworks.budget.services.connection.NetworkConnectivityObserver
import com.batsworks.budget.services.notification.NotificationChannelId
import com.batsworks.budget.services.notification.NotificationToast
import com.rollbar.android.Rollbar
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BudgetApplication : Application(), ImageLoaderFactory {

	private val tag = BudgetApplication::class.java.name

	companion object {
		lateinit var rollbar: Rollbar
	}

	override fun onCreate() {
		super.onCreate()
		rollbar = Rollbar(this)
//		notification()
//		networkState()
//		syncData()
	}

//	private fun roomDatabase() {
//		database = Room.databaseBuilder(
//			applicationContext,
//			Database::class.java,
//			Database.NAME
//		).setQueryCallback({ query, args ->
//			Log.d("QUERY", query)
//			if (args.isNotEmpty()) Log.d("ARGS", "$args")
//		}, Executors.newSingleThreadExecutor())
//			.fallbackToDestructiveMigration()
//			.build()
//	}

	private fun notification() {
		val notificationChannel = NotificationChannel(
			NotificationChannelId.CHANNEL.id,
			"Notification BatsWorks Budget",
			NotificationManager.IMPORTANCE_HIGH
		)

		notificationChannel.description = "A notification from you budget app"
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(notificationChannel)

	}

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

//	private fun syncData() {
//		val contraints = Constraints.Builder()
//			.setRequiredNetworkType(NetworkType.CONNECTED)
//			.build()
//
//		val workerRequest = PeriodicWorkRequestBuilder<SyncData>(
//			repeatInterval = 6,
//			repeatIntervalTimeUnit = TimeUnit.HOURS,
//			flexTimeInterval = 15,
//			flexTimeIntervalUnit = TimeUnit.MINUTES
//		).setBackoffCriteria(
//			backoffPolicy = BackoffPolicy.LINEAR,
//			duration = Duration.ofMinutes(1)
//		).setConstraints(contraints).addTag(AJUST_TAG(tag)).build()
//
//		val workManager = WorkManager.getInstance(this)
//		workManager.cancelAllWork()
//
//		workManager.enqueueUniquePeriodicWork(
//			AJUST_TAG(tag),
//			ExistingPeriodicWorkPolicy.UPDATE,
//			workerRequest
//		)
//		workManager.getWorkInfosForUniqueWorkLiveData(AJUST_TAG(tag))
//			.observeForever {
//				it.forEach { workInfo ->
//					Log.d("DATA_SYNC", "${workInfo.state}")
//				}
//			}
//
//	}

	private fun networkState() {
		val toast = NotificationToast(this)
		val networkConnectivityObserver = NetworkConnectivityObserver(this)
		networkConnectivityObserver.observeForever {
			Log.d("TEST", "MUDOU O ESTADO")
			if (it) toast.show("ligado")
			else toast.show("desligado")
		}
	}
}