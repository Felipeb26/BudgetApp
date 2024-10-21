package com.batsworks.budget.data.dto

import java.math.BigDecimal

data class AmountState(
	val current: BigDecimal,
	val future: BigDecimal,
	val charge: BigDecimal,
	val billing: BigDecimal,
)
