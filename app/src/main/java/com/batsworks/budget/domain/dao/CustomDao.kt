package com.batsworks.budget.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.batsworks.budget.domain.entity.UserEntity

@Dao
interface UsersDao{

	@Query("SELECT * FROM USERENTITY")
	fun findAll(): LiveData<List<UserEntity>>

	@Insert
	fun save(user:UserEntity)

	@Query("SELECT * FROM USERENTITY WHERE email=:email and password=:password")
	fun findByLogin(email:String, password:Int) : UserEntity?

	@Query("DELETE FROM USERENTITY WHERE id =:id")
	fun deleteUserById(id:Int)
}