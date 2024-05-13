package com.batsworks.budget.ui.view_model.add

import java.math.BigDecimal

sealed class AmountFormEvent {
    data class EntranceEventChange(val entrance: Boolean) : AmountFormEvent()
    data class ValueEventChange(val value: BigDecimal) : AmountFormEvent()
    data class ChargeNameEventChange(val chargeName: String) : AmountFormEvent()
    data class FileVoucher(val file: ByteArray) : AmountFormEvent()
    data object Submit : AmountFormEvent()
}