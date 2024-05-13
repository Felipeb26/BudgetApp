package com.batsworks.budget.domain.use_cases.login

import com.batsworks.budget.domain.use_cases.ValidationResult

class ValidateTerms {
	fun execute(checked: Boolean): ValidationResult {
		if (!checked) {
			return ValidationResult(false, "you must accept to continue")
		}
		return ValidationResult(true)
	}
}