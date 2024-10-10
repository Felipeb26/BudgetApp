package com.batsworks.budget.use_cases.amount

import com.batsworks.budget.use_cases.ValidationResult
import com.batsworks.budget.use_cases.Validator

class ValidateChargeName : Validator<String> {
	override fun execute(value: String): ValidationResult {
		if (value.isEmpty())
			return ValidationResult(false, "charge name must not be null")
		if (value.length < 3)
			return ValidationResult(false, "Charge Name must have at least 3 characteres")
		return ValidationResult(true)
	}
}