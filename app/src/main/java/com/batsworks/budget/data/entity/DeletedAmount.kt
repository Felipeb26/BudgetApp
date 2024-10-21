package com.batsworks.budget.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeletedAmount(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val localId: Int = 0,
    val firebaseId: String? = null
)

fun amountToDelete(amount: AmountEntity): DeletedAmount {
    return DeletedAmount(
        localId = amount.id,
        firebaseId = amount.firebaseId
    )
}