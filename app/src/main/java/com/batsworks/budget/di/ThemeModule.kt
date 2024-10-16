package com.batsworks.budget.di

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.batsworks.budget.ui.theme.CustomTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

	@Provides
	@Singleton
	fun providesTheme(@ApplicationContext context: Context): CustomTheme {
		return CustomTheme(context)
	}

}