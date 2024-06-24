package com.batsworks.budget.ui.view_model.add

import com.batsworks.budget.ui.view_model.ValidationResult

class ValidateFileVoucher {

    fun execute(file: ByteArray): ValidationResult {
        if (file.isEmpty())
            return ValidationResult(false, "file must not be empty")
        return ValidationResult(true)
    }

}