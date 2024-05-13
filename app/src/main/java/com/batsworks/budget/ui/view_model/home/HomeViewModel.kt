package com.batsworks.budget.ui.view_model.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.ajustTag
import com.batsworks.budget.components.currency
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.entity.AmountEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.math.BigDecimal
import java.time.Duration

class HomeViewModel(
	private var repository: AmountDao = BudgetApplication.database.getAmountDao(),
) : ViewModel() {

	private val tag = HomeViewModel::class.java.name
	private val _amountsMutableFlow = MutableStateFlow(emptyList<AmountEntity>())
	private val amounts = _amountsMutableFlow.asStateFlow()

	private val _profileCardValues = MutableStateFlow<Map<String, BigDecimal>>(HashMap())
	val totalAmount = _profileCardValues.asStateFlow()

	init {
		viewModelScope.launch {
			_amountsMutableFlow.emit(repository.findAll())
			calculateAmount()
		}
	}


	val lastAmounts = flow {
		while (true) {
			delay(Duration.ofSeconds(10))
			emit(repository.findLastAmounts())
			Log.d(ajustTag(tag), "rodou findAll")
		}
	}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(15000L), emptyList())


	private fun calculateAmount() = viewModelScope.launch {
		val map = HashMap<String, BigDecimal>()
		val entrace = findValues(amounts.value, true)
		val output = findValues(amounts.value, false)
		val remaining = entrace.minus(output)

		map["entrance"] = entrace
		map["output"] = output
		map["remaining"] = remaining
		_profileCardValues.emit(map)
	}

	fun showAmount(value: BigDecimal?, show: Boolean, word: MutableState<String>): String {
		word.value = if (show) currency(value) else ". . ."
		return word.value
	}

	private fun findValues(amounts: List<AmountEntity>, entrance: Boolean): BigDecimal {
		return amounts.filter { it.entrance == entrance }.map(AmountEntity::value).sumOf { it }
	}

}