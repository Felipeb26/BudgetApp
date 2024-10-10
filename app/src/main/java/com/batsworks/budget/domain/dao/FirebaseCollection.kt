package com.batsworks.budget.domain.dao

import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.UserFirebaseEntity

sealed class FirebaseCollection<T>(val path: String, val entity: Class<T>) {
	data object USERS :
		FirebaseCollection<UserFirebaseEntity>("users", UserFirebaseEntity::class.java)

	data object AMOUNTS :
		FirebaseCollection<AmountFirebaseEntity>("amounts", AmountFirebaseEntity::class.java)
}