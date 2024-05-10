package com.batsworks.budget.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.QueryDocumentSnapshot
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
)

fun querySnapshotToEntity(it: QueryDocumentSnapshot): UserEntity {
    return UserEntity(
        it["id"] as Int,
        it["nome"] as String,
        it["email"] as String,
        it["phone"] as String,
        it["password"] as Long,
    )
}

fun querySnapshotToEntity(it: Map<String, Any>): UserEntity {
    return UserEntity(
        nome = it["nome"] as String,
        email = it["email"] as String,
        phone = it["phone"] as String,
        password = it["password"] as Long,
    )
}