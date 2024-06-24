package com.batsworks.budget.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.batsworks.budget.components.functions.assertValues
import com.batsworks.budget.ui.theme.Theme
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
    val firebaseId: String = "",
    val loginWhenEnter: Boolean = false,
    val theme: String = Theme.CHERRY.theme
) {

    fun withName(nome: String?): UserEntity {
        return UserEntity(
            this.id,
            assertValues(nome, this.nome),
            this.email,
            this.phone,
            this.password,
            this.creatAt,
            this.firebaseId,
            this.loginWhenEnter,
            this.theme
        )
    }



    fun withEmail(email: String?): UserEntity {
        return UserEntity(
            this.id,
            this.nome,
            assertValues(email, this.email),
            this.phone,
            this.password,
            this.creatAt,
            this.firebaseId,
            this.loginWhenEnter,
            this.theme
        )
    }

    fun withPhone(phone: String?): UserEntity {
        return UserEntity(
            this.id,
            this.nome,
            this.email,
            assertValues(phone, this.phone),
            this.password,
            this.creatAt,
            this.firebaseId,
            this.loginWhenEnter,
            this.theme
        )
    }

    fun withPassword(password: Long?): UserEntity {
        return UserEntity(
            this.id,
            this.nome,
            this.email,
            this.phone,
            password ?: this.password,
            this.creatAt,
            this.firebaseId,
            this.loginWhenEnter,
            this.theme
        )
    }

    fun withPassword(password: String?): UserEntity {
        return UserEntity(
            this.id,
            this.nome,
            this.email,
            this.phone,
            assertValues(password, this.password.toString()).toLong(),
            this.creatAt,
            this.firebaseId,
            this.loginWhenEnter,
            this.theme
        )
    }

    fun withLoginWhenEnter(loginWhenEnter: Boolean): UserEntity {
        return UserEntity(
            this.id,
            this.nome,
            this.email,
            this.phone,
            this.password,
            this.creatAt,
            this.firebaseId,
            loginWhenEnter,
            this.theme
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