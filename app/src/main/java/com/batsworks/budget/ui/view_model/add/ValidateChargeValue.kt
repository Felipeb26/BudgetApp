package com.batsworks.budget.ui.view_model.add

import com.batsworks.budget.ui.view_model.ValidationResult
import java.math.BigDecimal

class ValidateChargeValue {

    fun execute(value: BigDecimal): ValidationResult {
        if (value == BigDecimal.ZERO)
            return ValidationResult(false, "value must be bigger than zero")
        if (value < BigDecimal.ZERO)
            return ValidationResult(false, "value must be a positive number")

        return ValidationResult(true)
    }
}