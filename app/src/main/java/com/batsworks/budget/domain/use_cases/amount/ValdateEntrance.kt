package com.batsworks.budget.domain.use_cases.amount

import com.batsworks.budget.domain.ValidationResult
import com.batsworks.budget.domain.Validator

class ValdateEntrance : Validator<Boolean?> {

    override fun execute(value: Boolean?): ValidationResult {
        if (value == null)
            return ValidationResult(false, "entrance must be provided")
        return ValidationResult(true)
    }

}