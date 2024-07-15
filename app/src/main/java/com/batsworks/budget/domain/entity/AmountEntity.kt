package com.batsworks.budget.domain.entity

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class AmountEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val chargeName: String = "",
	val value: BigDecimal = BigDecimal.ZERO,
	val entrance: Boolean = false,
	val file: ByteArray? = null,
	val user: String? = null,
	val extension: String = "",
	val size: Int = 0,
	val fileRef: Uri? = null,
	val amountDate: LocalDate = LocalDate.now(),
	val creatAt: LocalDateTime = LocalDateTime.now(),
	val isSync: Boolean = false,
) {

	fun withSync(isSync: Boolean?): AmountEntity {
		return AmountEntity(
			this.id,
			this.chargeName,
			this.value,
			this.entrance,
			this.file,
			this.user,
			this.extension,
			this.size,
			this.fileRef,
			this.amountDate,
			this.creatAt,
			isSync = isSync ?: false
		)
	}

	fun withRef(reference: Uri?): AmountEntity {
		return AmountEntity(
			this.id,
			this.chargeName,
			this.value,
			this.entrance,
			this.file,
			this.user,
			this.extension,
			this.size,
			reference,
			this.amountDate,
			this.creatAt,
		)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as AmountEntity

		if (file != null) {
			if (other.file == null) return false
			if (!file.contentEquals(other.file)) return false
		} else if (other.file != null) return false

		return true
	}

	override fun hashCode(): Int {
		return file?.contentHashCode() ?: 0
	}
}

fun isEntrance(amount: AmountEntity): Color {
	return if (amount.entrance) Color.Green else Color.Red
}
