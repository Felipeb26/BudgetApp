package com.batsworks.budget.ui.view_model.add

sealed class AmountFormEvent {
	data class EntranceEventChange(val entrance: Boolean) : AmountFormEvent()
	data class ValueEventChange(val value: String) : AmountFormEvent()
	data class ChargeNameEventChange(val chargeName: String) : AmountFormEvent()
	data class FileVoucher(val file: ByteArray) : AmountFormEvent()
	data class AmountDate(val amountDate: String) : AmountFormEvent()
	data object Submit : AmountFormEvent()
}