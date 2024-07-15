package com.batsworks.budget.domain.entity

import java.time.LocalDateTime

data class UserFirebaseEntity(
	val nome: String = "",
	val email: String = "",
	val phone: String = "",
	val password: Long = 0,
	val acceptedTerms: Boolean = false,
	val creatAt: LocalDateTime = LocalDateTime.now(),
) : AbstractEntity() {

	fun toEntity(): UserEntity {
		return UserEntity(
			nome = this.nome,
			email = this.email,
			phone = this.phone,
			password = this.password,
			creatAt = this.creatAt,
			firebaseId = this.id,
			isSync = true
		)
	}
}

fun toDTO(entity: UserEntity): UserFirebaseEntity {
	return UserFirebaseEntity(
		nome = entity.nome,
		email = entity.email,
		phone = entity.phone,
		password = entity.password,
		acceptedTerms = entity.termsAccepted,
		creatAt = entity.creatAt
	)
}