package com.batsworks.budget.ui.view_model.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.formatter.currency
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dto.AmountState
import com.batsworks.budget.domain.entity.AmountEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.future.future
import kotlinx.coroutines.time.delay
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDate

class HomeViewModel(
    private var repository: AmountDao = BudgetApplication.database.getAmountDao(),
) : ViewModel() {

    private val _profileCardValues = MutableStateFlow<AmountState?>(null)

    val lastAmounts = flow {
        emit(repository.findLastAmounts())
        while (true) {
            delay(Duration.ofSeconds(5))
            emit(repository.findLastAmounts())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3500), emptyList())

    val amountStateFlow = _profileCardValues.map {
        val allAmounts = repository.findAll()
        val entrace = findValues(allAmounts, entrance = true, today = false)
        val output = findValues(allAmounts, entrance = false, today = false)
        val remaining = entrace.minus(output)

        val current = findCurrentBalance(allAmounts)
        AmountState(
            current = current,
            future = current.add(remaining),
            charge = output,
            billing = entrace
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun showAmount(value: BigDecimal?, show: Boolean, word: MutableState<String>): String {
        if (show) {
            viewModelScope.future {
                delay(Duration.ofMillis(1500))
                word.value = currency(value)
            }
        } else word.value = ". . . . ."
        return word.value
    }

    private fun findValues(
        amounts: List<AmountEntity>,
        entrance: Boolean? = null,
        today: Boolean = false,
    ): BigDecimal {
        var filteredValues = amounts

        if (entrance != null) {
            filteredValues = filteredValues.filter { it.entrance == entrance }.toList()
        }

        filteredValues = filteredValues.filter {
            if (today) {
                return@filter it.amountDate.isBefore(LocalDate.now()) or
                        it.amountDate.isEqual(LocalDate.now())
            } else it.amountDate.isAfter(LocalDate.now())
        }.toList()

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