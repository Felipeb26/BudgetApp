package com.batsworks.budget.domain.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
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
    val creatAt: LocalDateTime = LocalDateTime.now(),
) {
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
	return if(amount.entrance) Color.Green else Color.Red
}
