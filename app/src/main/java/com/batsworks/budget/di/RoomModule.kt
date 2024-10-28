package com.batsworks.budget.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.batsworks.budget.data.dao.AmountDAO
import com.batsworks.budget.data.dao.Database
import com.batsworks.budget.data.dao.DeletedAmountDao
import com.batsworks.budget.data.dao.UsersDAO
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
		val database = Room.databaseBuilder(
			context, Database::class.java,
			Database.NAME
		).setQueryCallback({ query, args ->
			Log.d("QUERY", query)
			if (args.isNotEmpty()) Log.d("ARGS", "$args")
		}, Executors.newSingleThreadExecutor())
			.setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
			.fallbackToDestructiveMigration()
			.build()
		return database
	}

	@Provides
	@Singleton
	fun provideUserDao(database: Database): UsersDAO {
		return database.getUsersDao()
	}

	@Provides
	@Singleton
	fun provideAmountDao(database: Database): AmountDAO {
		return database.getAmountDao()
	}

	@Provides
	@Singleton
	fun provideDeletedAmountDao(database: Database): DeletedAmountDao {
		return database.getDeletedAmountDao()
	}

}