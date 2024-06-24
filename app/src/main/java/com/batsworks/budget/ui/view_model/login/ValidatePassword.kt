package com.batsworks.budget.ui.view_model.login

import com.batsworks.budget.ui.view_model.ValidationResult


class ValidatePassword {
	fun execute(password: String): ValidationResult {
		if (password.length <= 7) {
			return ValidationResult(false, "The password must have 8 caracters at least")
		}
		if (!password.any { it.isDigit() }) {
			return ValidationResult(false, "Password must have only numbers")
		}

		return ValidationResult(true)
	}
}