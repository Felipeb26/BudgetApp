package com.batsworks.budget.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.Database
import com.batsworks.budget.domain.dao.DeletedAmountDao
import com.batsworks.budget.domain.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext context: Context): Database {
		return Room.databaseBuilder(
			context, Database::class.java,
			Database.NAME
		).setQueryCallback({ query, args ->
			Log.d("QUERY", query)
			if (args.isNotEmpty()) Log.d("ARGS", "$args")
		}, Executors.newSingleThreadExecutor())
			.fallbackToDestructiveMigration()
			.build()
	}

	@Provides
	@Singleton
	fun provideUserDao(database: Database): UsersDao {
		return database.getUsersDao()
	}

	@Provides
	@Singleton
	fun provideAmountDao(database: Database): AmountDao {
		return database.getAmountDao()
	}

	@Provides
	@Singleton
	fun provideDeletedAmountDao(database: Database): DeletedAmountDao {
		return database.getDeletedAmountDao()
	}


}