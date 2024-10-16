package com.batsworks.budget.di

import android.content.Context
import com.batsworks.budget.ui.theme.CustomTheme
import com.batsworks.budget.ui.theme.THEME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ThemeModule {

	@Provides
	fun providesTheme(): THEME {
		return THEME.CHERRY
	}

	@Provides
	fun providesCustomTheme(@ActivityContext context: Context): CustomTheme {
		return CustomTheme(context)
	}

}