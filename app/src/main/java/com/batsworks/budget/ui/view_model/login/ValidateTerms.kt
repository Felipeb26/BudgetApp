package com.batsworks.budget.ui.view_model.login

import com.batsworks.budget.ui.view_model.ValidationResult

class ValidateTerms {
	fun execute(checked: Boolean): ValidationResult {
		if (!checked) {
			return ValidationResult(false, "you must accept to continue")
		}
		return ValidationResult(true)
	}
}