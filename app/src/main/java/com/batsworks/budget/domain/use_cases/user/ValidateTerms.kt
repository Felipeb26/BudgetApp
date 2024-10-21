package com.batsworks.budget.domain.use_cases.user

import com.batsworks.budget.domain.ValidationResult
import com.batsworks.budget.domain.Validator

class ValidateTerms : Validator<Boolean> {
	override fun execute(value: Boolean): ValidationResult {
		if (!value) {
			return ValidationResult(false, "you must accept to continue")
		}
		return ValidationResult(true)
	}
}