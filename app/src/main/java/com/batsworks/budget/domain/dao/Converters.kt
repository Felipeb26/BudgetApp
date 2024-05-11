package com.batsworks.budget.domain.dao

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

	@TypeConverter
	fun fromDate(now: LocalDateTime): String {
		return now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
	}

	@TypeConverter
	fun toDate(date: String): LocalDateTime {
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
}