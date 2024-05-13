package com.batsworks.budget.domain.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.batsworks.budget.domain.entity.AmountEntity

@Dao
interface AmountDao {

    @Query("SELECT * FROM AMOUNTENTITY")
    suspend fun findAll(): List<AmountEntity>

    @Query("SELECT * FROM AMOUNTENTITY LIMIT 6")
    suspend fun findLastAmounts(): List<AmountEntity>

    @Query("SELECT * FROM AMOUNTENTITY at WHERE at.id=:id")
    suspend fun findById(id: String): AmountEntity

    @Upsert
    suspend fun save(amout: AmountEntity)

    @Query("DELETE FROM AMOUNTENTITY WHERE id=:id")
    suspend fun delete(id: Int)
}