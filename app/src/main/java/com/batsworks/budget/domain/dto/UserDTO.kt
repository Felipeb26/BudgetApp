package com.batsworks.budget.domain.dto

import com.batsworks.budget.domain.entity.AbstractEntity
import com.batsworks.budget.domain.entity.UserEntity
import java.time.LocalDateTime

data class UserDTO(
	val nome: String,
	val email: String,
	val phone: String,
	val password: Long,
	val acceptedTerms: Boolean,
	val creatAt: LocalDateTime = LocalDateTime.now(),
) : AbstractEntity()

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