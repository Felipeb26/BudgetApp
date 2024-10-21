package com.batsworks.budget.data.dao

import android.net.Uri
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.math.RoundingMode
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
		return value.setScale(2, RoundingMode.HALF_UP).toDouble()
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