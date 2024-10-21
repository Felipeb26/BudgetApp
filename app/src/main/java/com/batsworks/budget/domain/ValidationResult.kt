package com.batsworks.budget.domain

interface Validator<T> {
	fun execute(value: T): ValidationResult
}

open class ValidationResult(
	val successful: Boolean,
	val errorMessage: String? = null,
)
