package com.batsworks.budget.domain.entity

import android.net.Uri
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate
import java.time.LocalDateTime

data class AmountFirebaseEntity(
	@DocumentId
	val id: String = "",
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
)

fun toDTO(amount: AmountEntity): AmountFirebaseEntity {
	return AmountFirebaseEntity(
		id = amount.firebaseId ?: "",
		chargeName = amount.chargeName,
		value = amount.value.toFloat(),
		entrance = amount.entrance,
		extension = amount.extension,
		user = amount.user ?: "",
		size = amount.size,
		fileRef = amount.fileRef,
		amountDate = amount.amountDate,
		creatAt = amount.creatAt,
		isSync = amount.isSync
	)
}
