package com.batsworks.budget.domain.dto

import java.math.BigDecimal

data class AmountState(
	val current: BigDecimal,
	val future: BigDecimal,
	val charge: BigDecimal,
	val billing: BigDecimal,
)
