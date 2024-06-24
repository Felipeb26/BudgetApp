package com.batsworks.budget.ui.view_model

data class ValidationResult(
	val successful: Boolean,
	val errorMessage: String? = null,
)
