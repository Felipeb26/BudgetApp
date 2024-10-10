package com.batsworks.budget.use_cases

interface Validator<T> {
	fun execute(value: T): ValidationResult
}

open class ValidationResult(
	val successful: Boolean,
	val errorMessage: String? = null,
)
