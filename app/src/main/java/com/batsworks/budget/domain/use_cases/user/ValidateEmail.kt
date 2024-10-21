package com.batsworks.budget.domain.use_cases.user

import android.util.Patterns
import com.batsworks.budget.domain.ValidationResult
import com.batsworks.budget.domain.Validator

class ValidateEmail : Validator<String> {
	override fun execute(value: String): ValidationResult {
		if (value.isBlank()) {
			return ValidationResult(false, "The email cant be blank")
		}
		if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
			return ValidationResult(false, "Thats not a valid email")
		}

		return ValidationResult(true)
	}

}