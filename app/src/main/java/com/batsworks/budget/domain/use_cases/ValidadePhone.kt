package com.batsworks.budget.domain.use_cases

class ValidatePhone {
	fun execute(phone: String): ValidationResult {
		if (phone.isBlank()) {
			return ValidationResult(false, "The phone cant be blank")
		}
		if (phone.length <= 5) {
			return ValidationResult(false, "Thats not a valid phone")
		}

		return ValidationResult(true)
	}
}
//https://www.youtube.com/watch?v=d3gzjfc0Zhg