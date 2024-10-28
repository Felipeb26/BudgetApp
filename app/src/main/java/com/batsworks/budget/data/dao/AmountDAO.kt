package com.batsworks.budget.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.batsworks.budget.data.entity.AmountEntity

@Dao
interface AmountDAO {

    @Query("SELECT * FROM AMOUNTENTITY ORDER BY amountDate DESC")
    suspend fun findAll(): List<AmountEntity>

    @Query("""
        SELECT ae.id, ae.chargeName, ae.value, ae.entrance, null as file, null as user, ae.extension,
            null as size, null as fileRef,ae.amountDate, ae.creatAt, ae.isSync, ae.firebaseId
        FROM AMOUNTENTITY ae ORDER BY amountDate, entrance DESC LIMIT 7 
    """)
    suspend fun findLastAmounts(): List<AmountEntity>

    @Query("SELECT * FROM AMOUNTENTITY at WHERE at.id=:id")
    suspend fun findById(id: Int): AmountEntity

    @Upsert
    suspend fun save(amout: AmountEntity)

    @Query("DELETE FROM AMOUNTENTITY WHERE id=:id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM AMOUNTENTITY WHERE isSync = 0")
    suspend fun findNotSync(): List<AmountEntity>

    @Query("SELECT ae.chargeName FROM AMOUNTENTITY ae WHERE ae.isSync = 1")
    suspend fun findAllAmountSync(): List<String>

}