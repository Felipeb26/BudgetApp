package com.batsworks.budget.domain.use_cases.amount

import com.batsworks.budget.domain.ValidationResult
import com.batsworks.budget.domain.Validator

class ValidateFileVoucher : Validator<ByteArray> {

    override fun execute(value: ByteArray): ValidationResult {
        if (value.isEmpty())
            return ValidationResult(false, "file must not be empty")
        return ValidationResult(true)
    }

}