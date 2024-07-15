package com.batsworks.budget.domain.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.batsworks.budget.domain.entity.UserEntity

@Dao
interface UsersDao {

    @Query("SELECT * FROM USERENTITY")
    suspend fun findAll(): List<UserEntity>

    @Query("SELECT * FROM USERENTITY LIMIT 1")
    suspend fun findUser(): UserEntity

    @Upsert
    suspend fun save(user: UserEntity)

    @Query("SELECT * FROM USERENTITY WHERE email=:email and password=:password")
    suspend fun findByLogin(email: String, password: Int): UserEntity?

    @Query("DELETE FROM USERENTITY WHERE id =:id")
    suspend fun deleteUserById(id: Int)

    @Query("UPDATE USERENTITY SET theme=:theme")
    suspend fun updateTheme(theme: String)

    @Query("SELECT * FROM USERENTITY WHERE isSync = 0")
    suspend fun findNotSyncData(): List<UserEntity>
}