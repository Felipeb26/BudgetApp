package com.batsworks.budget.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.time.LocalDateTime

//@Entity(tableName = "TB_USERS")
@Entity
data class UserEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val nome: String = "",
	val email: String = "",
	val phone: String = "",
	val password: Int? = null,
	val creatAt: LocalDateTime = LocalDateTime.now(),
)

fun querySnapshotToEntity(it: QueryDocumentSnapshot): UserEntity {
	val data = it["creatAt"]
	return UserEntity(
		it["id"] as Int,
		it["nome"] as String,
		it["email"] as String,
		it["phone"] as String,
		it["password"] as Int,
	)
}