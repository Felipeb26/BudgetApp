package com.batsworks.budget.ui.view_model.amout

import com.batsworks.budget.ui.view_model.ValidationResult

class ValdateEntrance {

    fun execute(entrance: Boolean): ValidationResult {
        if (entrance == null)
            return ValidationResult(false, "entrance must be provided")
        return ValidationResult(true)
    }

}