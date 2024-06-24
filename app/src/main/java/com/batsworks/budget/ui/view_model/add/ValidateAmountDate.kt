package com.batsworks.budget.ui.view_model.add

import com.batsworks.budget.components.localDate
import com.batsworks.budget.ui.view_model.ValidationResult
import java.time.LocalDate

class ValidateAmountDate {
	fun execute(date: String): ValidationResult {
		if (date.isEmpty())
			return ValidationResult(false, "amount date cant be empty")

		if(localDate(date).isBefore(LocalDate.now()))
			return ValidationResult(false, "amount date cant be used for earlier dates")

		return ValidationResult(true)
	}
}