package com.batsworks.budget.domain.use_cases.login

import com.batsworks.budget.domain.use_cases.ValidationResult


class ValidatePassword {
	fun execute(password: String): ValidationResult {
		if (password.length <= 7) {
			return ValidationResult(false, "The password must have 8 caracters at least")
		}
		if (!password.any { it.isDigit() }) {
			return ValidationResult(false, "Password must have at least a number")
		}

		return ValidationResult(true)
	}
}