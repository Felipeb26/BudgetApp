package com.batsworks.budget.domain.use_cases.user

import com.batsworks.budget.domain.ValidationResult
import com.batsworks.budget.domain.Validator


class ValidatePassword : Validator<String> {
	override fun execute(value: String): ValidationResult {
		if (value.length <= 7) {
			return ValidationResult(false, "The password must have 8 caracters at least")
		}
		if (!value.any { it.isDigit() }) {
			return ValidationResult(false, "Password must have only numbers")
		}

		return ValidationResult(true)
	}
}