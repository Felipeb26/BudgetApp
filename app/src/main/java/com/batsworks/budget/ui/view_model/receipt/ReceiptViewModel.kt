package com.batsworks.budget.ui.view_model.receipt

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.Resource
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.services.download.AndroidDownloader
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.URL

class ReceiptViewModel(
	context: Context,
	private val localRepository: AmountDao = BudgetApplication.database.getAmountDao(),
	private val download: AndroidDownloader? = AndroidDownloader(context),
	id: String,
) : ViewModel() {


	private val resourceEventChannel = Channel<Resource<Any>>()
	val resourceEventFlow = resourceEventChannel.receiveAsFlow()

	private val _mutableEntityAmount = MutableStateFlow<AmountEntity?>(null)
	var entityAmount = _mutableEntityAmount.asStateFlow()

	init {
		showImage(id)
	}

	private fun showImage(id: String) {
		viewModelScope.launch {
			val amount = localRepository.findById(id)
			_mutableEntityAmount.emit(amount)
		}
	}

	fun downloadImage(amountEntity: AmountEntity) {
		if (download == null) return
		viewModelScope.launch {
			resourceEventChannel.send(Resource.Loading())
			try {
				val file = amountEntity.file ?: return@launch
				val imagePath = download.downloadFromBytes(amountEntity.chargeName, file)
				resourceEventChannel.send(Resource.Loading(false))
				resourceEventChannel.send(Resource.Sucess(imagePath.toString().plus("|${amountEntity.chargeName}.jpeg")))
			} catch (e: Exception) {
				resourceEventChannel.send(Resource.Failure(e.message))
			}
		}
	}

}