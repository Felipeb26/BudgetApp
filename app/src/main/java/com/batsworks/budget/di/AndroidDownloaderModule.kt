package com.batsworks.budget.di

import android.content.Context
import com.batsworks.budget.services.download.AndroidDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AndroidDownloaderModule {

	@Provides
	@Singleton
	fun providesAndroidDownloader(@ApplicationContext context: Context): AndroidDownloader {
		return AndroidDownloader(context)
	}
}