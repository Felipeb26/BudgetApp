package com.batsworks.budget.domain.entity

import android.content.Context
import android.net.Uri
import com.batsworks.budget.components.files.image.getByteArrayFromUri
import com.google.firebase.firestore.DocumentId
import java.math.BigDecimal
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
) {
    fun toEntity(context: Context): AmountEntity {
        return AmountEntity(
            chargeName = this.chargeName,
            value = BigDecimal.valueOf(this.value.toDouble()),
            entrance = this.entrance,
            user = this.user,
            extension = this.extension,
            size = this.size,
            fileRef = this.fileRef,
            file = fileRef?.let { getByteArrayFromUri(context, it) },
            firebaseId = this.id,
            amountDate = this.amountDate,
            creatAt = this.creatAt
        )

    }

    fun toEntity(): AmountEntity {
        return AmountEntity(
            chargeName = this.chargeName,
            value = BigDecimal.valueOf(this.value.toDouble()),
            entrance = this.entrance,
            user = this.user,
            extension = this.extension,
            size = this.size,
            fileRef = this.fileRef,
            firebaseId = this.id,
            isSync = true,
            amountDate = this.amountDate,
            creatAt = this.creatAt
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
        fileRef = amount.fileRef,
        amountDate = amount.amountDate,
        creatAt = amount.creatAt,
        isSync = amount.isSync
    )
}
