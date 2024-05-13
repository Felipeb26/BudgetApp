package com.batsworks.budget.domain.use_cases.amout

import com.batsworks.budget.domain.use_cases.ValidationResult

class ValdateEntrance {

    fun execute(entrance: Boolean): ValidationResult {
        if (entrance == null)
            return ValidationResult(false, "entrance must be provided")
        return ValidationResult(true)
    }

}