package com.batsworks.budget.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.entity.DeletedAmount

@TypeConverters(Converters::class)
@Database(entities = [UserEntity::class, AmountEntity::class, DeletedAmount::class], version = 9, exportSchema = false)
abstract class Database : RoomDatabase() {

	companion object {
		const val NAME = "BUDGET_DB"
	}

	abstract fun getUsersDao(): UsersDao

	abstract fun getAmountDao(): AmountDao

	abstract fun getDeletedAmountDao(): DeletedAmountDao

}