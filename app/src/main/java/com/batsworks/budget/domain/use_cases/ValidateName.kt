package com.batsworks.budget.domain.use_cases

class ValidateName {
	fun execute(name: String): ValidationResult {
		if (name.isBlank()) {
			return ValidationResult(false, "The name cant be blank")
		}
		if (name.length <= 5) {
			return ValidationResult(false, "Thats not a valid name")
		}

		return ValidationResult(true)
	}
}