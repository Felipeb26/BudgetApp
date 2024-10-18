package com.batsworks.budget

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.getSystemService
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.DeletedAmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.services.notification.NotificationDataCreation
import com.batsworks.budget.services.worker.SyncData
import com.rollbar.android.Rollbar
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BudgetApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    companion object {
        lateinit var rollbar: Rollbar
    }

    override fun onCreate() {
        super.onCreate()
        rollbar = Rollbar(this)
        notification()
        WorkManager.initialize(
            this, Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setWorkerFactory(workerFactory)
                .build()
        )
    }


    private fun notification() {
        val notificationChannel = NotificationChannel(
            NotificationDataCreation.CHANNEL.content,
            NotificationDataCreation.NAME.content,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = NotificationDataCreation.DESCRIPTION.content
        val notificationManager = getSystemService<NotificationManager>()!!
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


}


class CustomWorkerFactory @Inject constructor(
    private val usersDao: UsersDao,
    private val amountDao: AmountDao,
    private val deletedAmountDao: DeletedAmountDao,
    private val amountRepository: CustomRepository<AmountFirebaseEntity>,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = SyncData(appContext,workerParameters, usersDao, amountDao, deletedAmountDao, amountRepository)

}