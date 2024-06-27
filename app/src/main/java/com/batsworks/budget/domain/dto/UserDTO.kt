package com.batsworks.budget.domain.dto

import com.batsworks.budget.domain.entity.AbstractEntity
import com.batsworks.budget.domain.entity.UserEntity
import java.time.LocalDateTime

data class UserDTO(
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
			firebaseId = this.id
		)
	}
}

fun toEntity(dto: UserDTO): UserEntity {
	return UserEntity(
		nome = dto.nome,
		email = dto.email,
		phone = dto.phone,
		password = dto.password,
		creatAt = dto.creatAt,
		firebaseId = dto.id
	)
}

fun toDTO(entity: UserEntity): UserDTO {
	return UserDTO(
		nome = entity.nome,
		email = entity.email,
		phone = entity.phone,
		password = entity.password,
		acceptedTerms = entity.termsAccepted,
		creatAt = entity.creatAt
	)
}