package com.batsworks.budget.use_cases.user

import com.batsworks.budget.use_cases.ValidationResult
import com.batsworks.budget.use_cases.Validator

class ValidateName : Validator<String> {
	override fun execute(value: String): ValidationResult {
		if (value.isBlank()) {
			return ValidationResult(false, "The name cant be blank")
		}
		if (value.length <= 5) {
			return ValidationResult(false, "Thats not a valid name")
		}

		return ValidationResult(true)
	}
}