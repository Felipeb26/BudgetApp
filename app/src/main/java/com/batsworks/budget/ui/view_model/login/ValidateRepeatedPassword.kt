package com.batsworks.budget.ui.view_model.login

import com.batsworks.budget.ui.view_model.ValidationResult


class ValidateRepeatedPassword {
	fun execute(password: String, repeatedPassword: String): ValidationResult {
		if (password.length <= 7) {
			return ValidationResult(false, "The password must have 8 caracters at least")
		}
		if (!password.any { it.isDigit() }) {
			return ValidationResult(false, "Thats must contain a number")
		}
		if(password != repeatedPassword){
			return ValidationResult(false, "Password are not the same")
		}

		return ValidationResult(true)
	}
}