package com.batsworks.budget.ui.view_model.history

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.data.dao.AmountDAO
import com.batsworks.budget.data.dao.DeletedAmountDAO
import com.batsworks.budget.data.entity.AmountEntity
import com.batsworks.budget.data.entity.DeletedAmount
import com.batsworks.budget.domain.Resource
import com.batsworks.budget.ui.components.menu.AJUST_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
	private val repository: AmountDAO,
	private val deletedAmountDao: DeletedAmountDAO,
) : ViewModel() {

	private val tag = HistoryViewModel::class.java.name
	val amounts = mutableStateOf(emptyList<AmountEntity>())

	private val resourceEventChannel = Channel<Resource<Any>>()
	val resourceEventFlow = resourceEventChannel.receiveAsFlow()

	init {
		viewModelScope.launch {
			delay(Duration.ofSeconds(2))
			amounts.value = repository.findAll()
			amounts.value.forEach {
				Log.d(tag, "${it.id} ${it.chargeName} ${it.amountDate}")
			}
		}
	}

	fun searchAmounts() = viewModelScope.launch {
		amounts.value = repository.findAll()
	}

	fun deleteAmount(id: Int) = viewModelScope.launch {
		resourceEventChannel.send(Resource.Loading(true))
		try {
			val amount = repository.findById(id)
			deletedAmountDao.save(DeletedAmount(localId = id, firebaseId = amount.firebaseId))
			repository.delete(id)
			resourceEventChannel.send(Resource.Loading(false))
			resourceEventChannel.send(Resource.Sucess(""))
		} catch (e: Exception) {
			Log.d(AJUST_TAG(tag), e.message ?: "an error has happen")
			resourceEventChannel.send(Resource.Loading(false))
			resourceEventChannel.send(Resource.Failure(e.message ?: ""))
		}
	}
}