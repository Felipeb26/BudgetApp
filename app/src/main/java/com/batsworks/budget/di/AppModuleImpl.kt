package com.batsworks.budget.di

import android.content.Context
import android.util.Log
import com.batsworks.budget.domain.dao.FirebaseCollection
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.UserFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository

interface AppModule {
	val amountRepository: CustomRepository<AmountFirebaseEntity>
	val userRepository: CustomRepository<UserFirebaseEntity>
}

class AppModuleImpl(appContext: Context) : AppModule {

	init {
		Log.d("APP_MODULE", "Module was Started by ${appContext.packageName}")
	}

	override val amountRepository: CustomRepository<AmountFirebaseEntity> by lazy {
		CustomRepository(FirebaseCollection.AMOUNTS, AmountFirebaseEntity::class.java)
	}

	override val userRepository: CustomRepository<UserFirebaseEntity> by lazy {
		CustomRepository(FirebaseCollection.USERS, UserFirebaseEntity::class.java)
	}

}