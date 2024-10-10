package com.batsworks.budget.use_cases.user

import android.util.Patterns
import com.batsworks.budget.use_cases.ValidationResult
import com.batsworks.budget.use_cases.Validator

class ValidatePhone : Validator<String> {
    override fun execute(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(false, "The phone cant be blank")
        }
        if (!Patterns.PHONE.matcher(value).matches()) {
            return ValidationResult(false, "Thats not a valid phone")
        }

        return ValidationResult(true)
    }
}