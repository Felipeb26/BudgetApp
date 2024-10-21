package com.batsworks.budget.domain.use_cases.amount

import com.batsworks.budget.components.formatter.localDate
import com.batsworks.budget.domain.ValidationResult
import com.batsworks.budget.domain.Validator
import java.time.LocalDate

class ValidateAmountDate : Validator<String> {
	override fun execute(value: String): ValidationResult {
		if (value.isEmpty())
			return ValidationResult(false, "amount date cant be empty")

		if(localDate(value).isBefore(LocalDate.now()))
			return ValidationResult(false, "amount date cant be used for earlier dates")

		return ValidationResult(true)
	}
}