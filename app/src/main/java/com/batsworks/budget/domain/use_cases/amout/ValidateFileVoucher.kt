package com.batsworks.budget.domain.use_cases.amout

import com.batsworks.budget.domain.use_cases.ValidationResult

class ValidateFileVoucher {

    fun execute(file: ByteArray): ValidationResult {
        if (file.isEmpty())
            return ValidationResult(false, "file must not be empty")
        return ValidationResult(true)
    }

}