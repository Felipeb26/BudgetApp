package com.batsworks.budget.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.batsworks.budget.data.entity.UserEntity
import com.batsworks.budget.data.entity.AmountEntity
import com.batsworks.budget.data.entity.DeletedAmount

@TypeConverters(Converters::class)
@Database(entities = [UserEntity::class, AmountEntity::class, DeletedAmount::class], version = 9, exportSchema = false)
abstract class Database : RoomDatabase() {

	companion object {
		const val NAME = "BUDGET_DB"
	}

	abstract fun getUsersDao(): UsersDAO

	abstract fun getAmountDao(): AmountDAO

	abstract fun getDeletedAmountDao(): DeletedAmountDao

}