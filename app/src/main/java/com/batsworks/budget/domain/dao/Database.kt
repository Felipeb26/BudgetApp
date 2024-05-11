package com.batsworks.budget.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.entity.AmountEntity

@TypeConverters(Converters::class)
@Database(entities = [UserEntity::class, AmountEntity::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

	companion object {
		const val NAME = "BUDGET_DB"
	}

	abstract fun getUsersDao(): UsersDao

	abstract fun getAmountDao(): AmountDao

}