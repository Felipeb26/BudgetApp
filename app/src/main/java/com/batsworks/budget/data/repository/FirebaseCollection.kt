package com.batsworks.budget.data.repository

import com.batsworks.budget.data.entity.AmountFirebaseEntity
import com.batsworks.budget.data.entity.UserFirebaseEntity

sealed class FirebaseCollection<T>(val path: String, val entity: Class<T>) {
	data object USERS :
		FirebaseCollection<UserFirebaseEntity>("users", UserFirebaseEntity::class.java)

	data object AMOUNTS :
		FirebaseCollection<AmountFirebaseEntity>("amounts", AmountFirebaseEntity::class.java)
}