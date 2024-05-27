package com.batsworks.budget.ui.view_model.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.ajustTag
import com.batsworks.budget.components.currency
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dto.AmountState
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
import java.time.LocalDate

class HomeViewModel(
	private var repository: AmountDao = BudgetApplication.database.getAmountDao(),
) : ViewModel() {

	private val tag = HomeViewModel::class.java.name
	private val _amountsMutableFlow = MutableStateFlow(emptyList<AmountEntity>())
	private val amounts = _amountsMutableFlow.asStateFlow()

	private val _profileCardValues = MutableStateFlow<AmountState?>(null)
	val totalAmount = _profileCardValues.asStateFlow()

	init {
		viewModelScope.launch {
			repository.let { _amountsMutableFlow.emit(it.findAll()) }
			calculateAmount()
		}
	}


	val lastAmounts = flow {
		emit(repository.findLastAmounts())
		while (true) {
			delay(Duration.ofSeconds(5))
			emit(repository.findLastAmounts())
			calculateAmount()
		}
	}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3500), emptyList())


	private fun calculateAmount() = viewModelScope.launch {
		val allAmounts = repository.findAll()
		val entrace = findValues(allAmounts, entrance = true, today = false)
		val output = findValues(allAmounts, entrance = false, today = false)
		val remaining = entrace.minus(output)

		val current = findCurrentBalance(amounts.value)
		val amount = AmountState(
			current = current,
			future = current.add(remaining),
			charge = output,
			billing = entrace
		)
		_profileCardValues.emit(amount)
	}

	fun showAmount(value: BigDecimal?, show: Boolean, word: MutableState<String>): String {
		word.value = if (show) currency(value) else ". . ."
		return word.value
	}

	private fun findValues(
		amounts: List<AmountEntity>,
		entrance: Boolean? = null,
		today: Boolean? = null,
	): BigDecimal {
		var filteredValues = amounts

		if (entrance != null) {
			filteredValues = filteredValues.filter { it.entrance == entrance }.toList()
		}

		if (today != null) {
			filteredValues = filteredValues.filter {
				if (today) {
					return@filter it.amountDate.isBefore(LocalDate.now()) or
							it.amountDate.isEqual(LocalDate.now())
				} else it.amountDate.isAfter(LocalDate.now())
			}.toList()
		}

		return filteredValues.map(AmountEntity::value).sumOf { it }
	}

	private fun findCurrentBalance(amounts: List<AmountEntity>): BigDecimal {
		val negativeValue = amounts.filter { !it.entrance }
			.filter {
				it.amountDate.isBefore(LocalDate.now()) or it.amountDate.isEqual(LocalDate.now())
			}.map(AmountEntity::value).sumOf { it }

		val positiveValue = amounts.filter { it.entrance }
			.filter {
				it.amountDate.isBefore(LocalDate.now()) or it.amountDate.isEqual(LocalDate.now())
			}.map(AmountEntity::value).sumOf { it }
		return positiveValue.minus(negativeValue)
	}

}