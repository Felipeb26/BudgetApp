package com.batsworks.budget.domain.dao

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

	@TypeConverter
	fun fromDate(now: LocalDateTime): String {
		return now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
	}

	@TypeConverter
	fun toDate(date: String): LocalDateTime {
		return LocalDateTime.parse(date)
	}
}