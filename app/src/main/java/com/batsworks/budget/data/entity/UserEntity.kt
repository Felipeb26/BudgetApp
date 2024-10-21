package com.batsworks.budget.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.batsworks.budget.components.functions.assertValues
import com.batsworks.budget.ui.theme.custom.THEME
import java.time.LocalDateTime

@Entity
data class UserEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val nome: String = "",
	val email: String = "",
	val phone: String = "",
	val password: Long = 0,
	val termsAccepted: Boolean = false,
	val creatAt: LocalDateTime = LocalDateTime.now(),
	val firebaseId: String = "",
	val loginWhenEnter: Boolean = false,
	val theme: String = THEME.CHERRY.theme,
	val isSync: Boolean = false,
) {

	fun withSync(isSync: Boolean?): UserEntity {
		return UserEntity(
			this.id,
			assertValues(nome, this.nome),
			this.email,
			this.phone,
			this.password,
			this.termsAccepted,
			this.creatAt,
			this.firebaseId,
			this.loginWhenEnter,
			this.theme,
			isSync = isSync ?: false
		)
	}

	fun withName(nome: String?): UserEntity {
		return UserEntity(
			this.id,
			assertValues(nome, this.nome),
			this.email,
			this.phone,
			this.password,
			this.termsAccepted,
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
			this.termsAccepted,
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
			this.termsAccepted,
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
			this.termsAccepted,
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
			this.termsAccepted,
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
			this.termsAccepted,
			this.creatAt,
			this.firebaseId,
			loginWhenEnter,
			this.theme
		)
	}
}