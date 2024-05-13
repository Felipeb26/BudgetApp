package com.batsworks.budget.domain.use_cases.amout

import com.batsworks.budget.domain.use_cases.ValidationResult

class ValidateChargeName {
    fun execute(string: String): ValidationResult {
        if (string.isEmpty())
            return ValidationResult(false, "charge name must not be null")
        if (string.length < 3)
            return ValidationResult(false, "Charge Name must have at least 3 characteres")
        return ValidationResult(true)
    }
}