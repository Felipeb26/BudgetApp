package com.batsworks.budget.di

import android.app.NotificationManager
import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.batsworks.budget.services.notification.NotificationSnackBar
import com.batsworks.budget.services.notification.NotificationToast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

	@Provides
	@Singleton
	fun providesNotificationManager(@ApplicationContext context: Context): NotificationManager{
		return context.getSystemService(NotificationManager::class.java)
	}

	@Provides
	@Singleton
	fun provideToast(@ApplicationContext context: Context): NotificationToast {
		return NotificationToast(context)
	}

	@Provides
	@Singleton
	fun providesSnackBar(
		coroutineScope: CoroutineScope,
		snackBarHostState: SnackbarHostState,
	): NotificationSnackBar {
		return NotificationSnackBar(coroutineScope, snackBarHostState)
	}
}