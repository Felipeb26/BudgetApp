package com.batsworks.budget.ui.view_model.history

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.ajustTag
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.entity.AmountEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
	private val repository: AmountDao = BudgetApplication.database.getAmountDao(),
) : ViewModel() {

	private val tag = HistoryViewModel::class.java.name
	val amounts = mutableStateOf(emptyList<AmountEntity>())
	private val resourceEventChannel = Channel<Resource<Any>>()
	val resourceEventFlow = resourceEventChannel.receiveAsFlow()

	fun init() {
		viewModelScope.launch {
			amounts.value = repository.findLastAmounts()
		}
	}

	fun deleteAmount(id: Int) = viewModelScope.launch {
		resourceEventChannel.send(Resource.Loading(true))
		try {
			repository.delete(id)
			resourceEventChannel.send(Resource.Loading(false))
			resourceEventChannel.send(Resource.Sucess(""))
		} catch (e: Exception) {
			Log.d(ajustTag(tag), e.message ?: "an error has happen")
			resourceEventChannel.send(Resource.Loading(false))
			resourceEventChannel.send(Resource.Failure(e.message ?: ""))
		}
	}
}