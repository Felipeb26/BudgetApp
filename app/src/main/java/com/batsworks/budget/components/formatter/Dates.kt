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

fun fromMillis(millis: Long, zone: ZoneId? = ZoneId.systemDefault()): LocalDateTime {
	return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zone)
}

fun fromMillis(millis: Long): LocalDate {
	return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
}

fun toMillis(date: LocalDateTime): Long {
	return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun toMillis(date: LocalDate): Long {
	return date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}