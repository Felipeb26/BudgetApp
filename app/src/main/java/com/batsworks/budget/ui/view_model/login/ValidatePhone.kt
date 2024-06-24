package com.batsworks.budget.ui.view_model.login

import android.util.Patterns
import com.batsworks.budget.ui.view_model.ValidationResult

class ValidatePhone {
    fun execute(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult(false, "The phone cant be blank")
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            return ValidationResult(false, "Thats not a valid phone")
        }

        return ValidationResult(true)
    }
}