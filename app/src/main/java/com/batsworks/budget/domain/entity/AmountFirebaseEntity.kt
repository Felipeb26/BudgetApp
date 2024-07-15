package com.batsworks.budget.domain.entity

import android.net.Uri
import java.time.LocalDate
import java.time.LocalDateTime

data class AmountFirebaseEntity(
	val chargeName: String = "",
	val value: Float = 0f,
	val entrance: Boolean = false,
	val user: String = "",
	val extension: String = "",
	val size: Int = 0,
	val fileRef: Uri? = null,
	val amountDate: LocalDate = LocalDate.now(),
	val creatAt: LocalDateTime = LocalDateTime.now(),
	val isSync: Boolean = false,
) : AbstractEntity()

fun toDTO(amount: AmountEntity): AmountFirebaseEntity {
	return AmountFirebaseEntity(
		chargeName = amount.chargeName,
		value = amount.value.toFloat(),
		entrance = amount.entrance,
		extension = amount.extension,
		size = amount.size,
		fileRef = amount.fileRef,
		amountDate = amount.amountDate,
		creatAt = amount.creatAt,
		isSync = amount.isSync
	)
}
