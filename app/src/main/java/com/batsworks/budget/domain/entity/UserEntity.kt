package com.batsworks.budget.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.auth.User
import java.time.LocalDateTime

@Entity
data class UserEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val nome: String = "",
	val email: String = "",
	val phone: String = "",
	val password: Long = 0,
	val creatAt: LocalDateTime = LocalDateTime.now(),
	val firebaseId: String = "",
	val loginWhenEnter: Boolean = false,
) {
	fun withLoginWhenEnter(loginWhenEnter: Boolean): UserEntity {
		return UserEntity(
			this.id,
			this.nome,
			this.email,
			this.phone,
			this.password,
			this.creatAt,
			this.firebaseId,
			loginWhenEnter
		)
	}
}


fun querySnapshotToEntity(it: QueryDocumentSnapshot): UserEntity {
	return UserEntity(
		it["id"] as Int,
		it["nome"] as String,
		it["email"] as String,
		it["phone"] as String,
		it["password"] as Long,
		firebaseId = it.id
	)
}

fun querySnapshotToEntity(
	it: Map<String, Any>,
	id: String,
	enterWhenOpen: Boolean = false,
): UserEntity {
	return UserEntity(
		nome = it["nome"] as String,
		email = it["email"] as String,
		phone = it["phone"] as String,
		password = it["password"] as Long,
		firebaseId = id,
		loginWhenEnter = enterWhenOpen
	)
}