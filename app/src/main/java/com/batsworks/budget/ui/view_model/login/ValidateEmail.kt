package com.batsworks.budget.ui.view_model.login

import android.util.Patterns
import com.batsworks.budget.ui.view_model.ValidationResult

class ValidateEmail {
	fun execute(email: String): ValidationResult {
		if (email.isBlank()) {
			return ValidationResult(false, "The email cant be blank")
		}
		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			return ValidationResult(false, "Thats not a valid email")
		}

		return ValidationResult(true)
	}
}