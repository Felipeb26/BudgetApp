package com.batsworks.budget.components.formatter

import android.icu.text.NumberFormat
import android.icu.util.ULocale
import java.math.BigDecimal
import java.util.Locale

fun currency(value: BigDecimal?): String {
	val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt-BR"))
	return currencyFormat.format(value ?: BigDecimal.ZERO)
}

fun currency(value: String?): String {
	val finalValue = if (value.isNullOrEmpty()) BigDecimal.ZERO else BigDecimal(value)
	val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt-BR"))
	return currencyFormat.format(finalValue)
}

fun currency(value: BigDecimal, locale: ULocale): String {
	val currencyFormat = NumberFormat.getCurrencyInstance(locale)
	return currencyFormat.format(value)
}
