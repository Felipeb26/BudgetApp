package com.batsworks.budget.domain.use_cases.login

import android.util.Patterns
import com.batsworks.budget.domain.use_cases.ValidationResult

class ValidatePhone {
	fun execute(phone: String): ValidationResult {
		if (phone.isBlank()) {
			return ValidationResult(false, "The phone cant be blank")
		}
		if (!Patterns.PHONE.matcher(phone).matches()) {
			return ValidationResult(false, "Thats not a valid phone")
		}

		return ValidationResult(true)
	}
}