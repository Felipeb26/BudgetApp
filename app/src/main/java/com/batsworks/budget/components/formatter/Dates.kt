package com.batsworks.budget.components.formatter

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatter(): DateTimeFormatter {
	return DateTimeFormatter.ofPattern("dd/MM/yyyy")
}

fun formatter(pattern: String): DateTimeFormatter {
	return DateTimeFormatter.ofPattern(pattern)
}

fun localDate(date: LocalDate?, pattern: String = "dd/MM/yyyy"): String {
	if (date == null) return LocalDate.now().format(formatter())
	return DateTimeFormatter.ofPattern(pattern).format(date)
}

fun localDate(date: String?, pattern: String = "dd/MM/yyyy"): LocalDate {
	if (date.isNullOrEmpty()) return LocalDate.now()
	return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern))
}

fun fromMillis(millis:Long, zone){
	LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of())
}