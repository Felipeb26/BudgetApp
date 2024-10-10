package com.batsworks.budget.use_cases.user

import com.batsworks.budget.use_cases.ValidationResult
import com.batsworks.budget.use_cases.Validator

class ValidateTerms : Validator<Boolean> {
	override fun execute(value: Boolean): ValidationResult {
		if (!value) {
			return ValidationResult(false, "you must accept to continue")
		}
		return ValidationResult(true)
	}
}