package com.batsworks.budget.domain.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.batsworks.budget.domain.entity.AmountEntity

@Dao
interface AmountDao {

	@Query("SELECT * FROM AMOUNTENTITY")
	suspend fun findAll(): List<AmountEntity>

	@Query("SELECT * FROM AMOUNTENTITY WHERE id=:id")
	suspend fun findByUserId(id: String)

	@Upsert
	suspend fun save(amout: AmountEntity)

	@Query("DELETE FROM AMOUNTENTITY WHERE id=:id")
	suspend fun delete(id: Int)
}