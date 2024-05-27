package com.batsworks.budget.components

import android.icu.text.NumberFormat
import android.icu.util.ULocale
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.util.Locale


fun formatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy")
}

fun formatter(pattern: String): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(pattern)
}

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
