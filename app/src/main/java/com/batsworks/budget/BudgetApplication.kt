package com.batsworks.budget

import android.app.Application
import androidx.room.Room
import com.batsworks.budget.domain.dao.Database

class BudgetApplication : Application() {

	companion object {
		lateinit var database: Database
	}

	override fun onCreate() {
		super.onCreate()
		database = Room.databaseBuilder(
			applicationContext,
			Database::class.java,
			Database.NAME
		).allowMainThreadQueries().build()
	}

}