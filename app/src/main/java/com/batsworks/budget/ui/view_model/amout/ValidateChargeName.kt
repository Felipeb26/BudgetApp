package com.batsworks.budget.ui.view_model.amout

import com.batsworks.budget.ui.view_model.ValidationResult

class ValidateChargeName {
    fun execute(string: String): ValidationResult {
        if (string.isEmpty())
            return ValidationResult(false, "charge name must not be null")
        if (string.length < 3)
            return ValidationResult(false, "Charge Name must have at least 3 characteres")
        return ValidationResult(true)
    }
}