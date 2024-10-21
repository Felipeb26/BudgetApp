package com.batsworks.budget.data.entity

import android.net.Uri
import com.batsworks.budget.components.formatter.fromMillis
import com.batsworks.budget.components.formatter.toMillis
import com.google.firebase.firestore.DocumentId
import java.math.BigDecimal

data class AmountFirebaseEntity(
    @DocumentId
    val id: String = "",
    val chargeName: String = "",
    val value: Float = 0f,
    val entrance: Boolean = false,
    val user: String = "",
    val extension: String = "",
    val size: Int = 0,
    val fileRef: String? = null,
    val amountDate: Long = 0,
    val creatAt: Long = 0,
    val isSync: Boolean = false,
) {

    fun toEntity(): AmountEntity {
        return AmountEntity(
            chargeName = this.chargeName,
            value = BigDecimal.valueOf(this.value.toDouble()),
            entrance = this.entrance,
            user = this.user,
            extension = this.extension,
            size = this.size,
            fileRef = this.fileRef?.let { Uri.parse(it) },
            firebaseId = this.id,
            amountDate = fromMillis(this.amountDate),
            creatAt = fromMillis(this.creatAt, null)
        )
    }
}

fun toDTO(amount: AmountEntity): AmountFirebaseEntity {
    return AmountFirebaseEntity(
        id = amount.firebaseId ?: "",
        chargeName = amount.chargeName,
        value = amount.value.toFloat(),
        entrance = amount.entrance,
        extension = amount.extension,
        user = amount.user ?: "",
        size = amount.size,
        fileRef = amount.fileRef?.toString(),
        amountDate = toMillis(amount.amountDate),
        creatAt = toMillis(amount.creatAt),
        isSync = amount.isSync
    )
}
