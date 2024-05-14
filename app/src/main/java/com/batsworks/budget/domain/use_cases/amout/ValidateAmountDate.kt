package com.batsworks.budget.domain.use_cases.amout

import com.batsworks.budget.components.localDate
import com.batsworks.budget.domain.use_cases.ValidationResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ValidateAmountDate {
	fun execute(date: String): ValidationResult {
		if (date.isEmpty())
			return ValidationResult(false, "amount date cant be empty")

		if(localDate(date).isBefore(LocalDate.now()))
			return ValidationResult(false, "amount date cant be used for earlier dates")

		return ValidationResult(true)
	}
}