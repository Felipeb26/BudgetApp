package com.batsworks.budget.use_cases.amount

import com.batsworks.budget.use_cases.ValidationResult
import com.batsworks.budget.use_cases.Validator

class ValidateFileVoucher : Validator<ByteArray> {

    override fun execute(value: ByteArray): ValidationResult {
        if (value.isEmpty())
            return ValidationResult(false, "file must not be empty")
        return ValidationResult(true)
    }

}