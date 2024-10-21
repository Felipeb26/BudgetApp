package com.batsworks.budget.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.batsworks.budget.data.entity.DeletedAmount

@Dao
interface DeletedAmountDao {

    @Query("SELECT * FROM DELETEDAMOUNT")
    suspend fun findAll(): List<DeletedAmount>
    @Query("SELECT COUNT(*) FROM DELETEDAMOUNT")
    suspend fun countAll(): Int

    @Query("SELECT * FROM DELETEDAMOUNT WHERE id=:id")
    suspend fun findById(id:Int): DeletedAmount

    @Query("SELECT * FROM DELETEDAMOUNT WHERE localId=:id")
    suspend fun findByIdLocal(id: Int): DeletedAmount

    @Query("SELECT * FROM DELETEDAMOUNT WHERE firebaseId=:id")
    suspend fun findByRemoteId(id:String): DeletedAmount

    @Upsert
    suspend fun save(toDeletedAmount: DeletedAmount)

    @Query("DELETE FROM DELETEDAMOUNT WHERE id=:id")
    suspend fun deleteById(id:Int)

    @Query("DELETE FROM DELETEDAMOUNT WHERE firebaseId=:id")
    suspend fun deleteByFirebaseId(id:String)
}