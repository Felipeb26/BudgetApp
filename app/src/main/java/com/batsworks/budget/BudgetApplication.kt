package com.batsworks.budget

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.room.Room
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.batsworks.budget.services.notification.NotificationChannelId
import com.batsworks.budget.domain.dao.Database
import java.util.concurrent.Executors

class BudgetApplication : Application(), ImageLoaderFactory {

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

	}

}