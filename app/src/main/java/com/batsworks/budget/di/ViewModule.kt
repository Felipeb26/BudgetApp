//package com.batsworks.budget.di
//
//import android.app.Activity
//import android.content.Context
//import android.view.View
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.android.qualifiers.ActivityContext
//
//@Module
//@InstallIn(ActivityComponent::class)
//object ViewModule {
//
//	@Provides
//	fun provideActivityView(@ActivityContext context: Context): View {
//		return (context as Activity).findViewById(android.R.id.content)
//	}
//}