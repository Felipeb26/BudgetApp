package com.batsworks.budget.ui.view_model.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.entity.AmountEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class HomeViewModel(
    private val repository: AmountDao = BudgetApplication.database.getAmountDao()
) : ViewModel() {

    private val tag = HomeViewModel::class.java.name
    val amounts = mutableStateOf(emptyList<AmountEntity>())

    val valores = mutableStateOf(emptyList<AmountEntity>())


    val _balance = MutableStateFlow<HashMap<String, BigDecimal>?>(null)
    val balance = _balance.asStateFlow()

    fun init() = viewModelScope.launch {
        val amountList =  repository.findLastAmounts()
        amounts.value = amountList
        valores.value = amountList
    }

    fun balance() = viewModelScope.launch {
        val amounts = repository.findAll()
        val goodValue = amounts.filter { it.entrance }.map(AmountEntity::value)
            .fold(BigDecimal.ZERO, BigDecimal::add)
        val downValue = amounts.filter { !it.entrance }.map(AmountEntity::value).sumOf { it }
        val map = HashMap<String, BigDecimal>()
        map["entrance"] = goodValue

//        valores = listOf(Balance(true, valor = goodValue))

        _balance.emit(map)
        Log.d(tag, "$goodValue")
    }

    data class Balance(
        val atual: Boolean = false,
        val futuro: Boolean = false,
        val gastos: Boolean = false,
        val valor: BigDecimal = BigDecimal.ZERO
    )

}