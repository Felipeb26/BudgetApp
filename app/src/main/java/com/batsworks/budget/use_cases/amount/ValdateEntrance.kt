package com.batsworks.budget.use_cases.amount

import com.batsworks.budget.use_cases.ValidationResult
import com.batsworks.budget.use_cases.Validator

class ValdateEntrance : Validator<Boolean?> {

    override fun execute(value: Boolean?): ValidationResult {
        if (value == null)
            return ValidationResult(false, "entrance must be provided")
        return ValidationResult(true)
    }

}