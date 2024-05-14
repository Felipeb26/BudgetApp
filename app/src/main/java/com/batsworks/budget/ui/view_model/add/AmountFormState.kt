package com.batsworks.budget.ui.view_model.add

import java.math.BigDecimal

data class AmountFormState(
	val entrance: Boolean = false,
	val entranceError: String? = null,
	val value: BigDecimal = BigDecimal.ZERO,
	val valueError: String? = null,
	val chargeName: String = "",
	val chargeNameError: String? = null,
	val amountDate: String = "",
	val amountDateError: String? = null,
	val file: ByteArray? = null,
	val fileError: String? = null,
)