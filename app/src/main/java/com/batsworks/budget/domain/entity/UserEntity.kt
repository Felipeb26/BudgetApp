package com.batsworks.budget.domain.entity

data class UserEntity(
	val nome: String = "",
	val email: String = "",
	val phone: String = "",
	val password: Int? = null,
)
