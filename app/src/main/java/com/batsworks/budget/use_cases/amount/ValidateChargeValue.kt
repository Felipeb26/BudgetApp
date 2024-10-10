package com.batsworks.budget.use_cases.amount

import com.batsworks.budget.use_cases.ValidationResult
import com.batsworks.budget.use_cases.Validator
import java.math.BigDecimal

class ValidateChargeValue : Validator<BigDecimal> {

    override fun execute(value: BigDecimal): ValidationResult {
        if (value == BigDecimal.ZERO)
            return ValidationResult(false, "value must be bigger than zero")
        if (value < BigDecimal.ZERO)
            return ValidationResult(false, "value must be a positive number")

        return ValidationResult(true)
    }
}