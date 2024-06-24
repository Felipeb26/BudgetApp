package com.batsworks.budget.domain.dao

import android.net.Uri
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

	@TypeConverter
	fun fromDate(now: LocalDate): String {
		return now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
	}

	@TypeConverter
	fun toDate(date: String): LocalDate {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
	}

	@TypeConverter
	fun fromDateTime(now: LocalDateTime): String {
		return now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
	}

	@TypeConverter
	fun toDateTime(date: String): LocalDateTime {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
	}

	@TypeConverter
	fun fromBigDecimal(value: BigDecimal): Double {
		return value.toDouble()
	}

	@TypeConverter
	fun toBigDecimal(value: Double): BigDecimal {
		return BigDecimal.valueOf(value)
	}

	@TypeConverter
	fun fromUri(value: Uri): String {
		return value.toString()
	}

	@TypeConverter
	fun toUri(value: String): Uri {
		return Uri.parse(value)
	}

}