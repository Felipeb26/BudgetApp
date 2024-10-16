package com.batsworks.budget.ui.view_model.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.files.getFileType
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.services.download.AndroidDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
	private val localRepository: AmountDao,
	private val download: AndroidDownloader,
) : ViewModel() {


	private val resourceEventChannel = Channel<Resource<Any>>()
	val resourceEventFlow = resourceEventChannel.receiveAsFlow()

	private val _mutableEntityAmount = MutableStateFlow<AmountEntity?>(null)
	var entityAmount = _mutableEntityAmount.asStateFlow()

	fun showImage(id: String) {
		viewModelScope.launch {
			val amount = localRepository.findById(id.toInt())
			_mutableEntityAmount.emit(amount)
		}
	}

	fun downloadImage(amountEntity: AmountEntity) {
		viewModelScope.launch {
			resourceEventChannel.send(Resource.Loading(true))
			try {
				val file = amountEntity.file ?: return@launch
				val type = getFileType(file)
				val imagePath = download.downloadFromBytes(amountEntity.chargeName, file)
				resourceEventChannel.send(Resource.Loading(false))
				resourceEventChannel.send(Resource.Sucess(imagePath.plus("|${amountEntity.chargeName}.$type")))
			} catch (e: Exception) {
				resourceEventChannel.send(Resource.Loading(false))
				resourceEventChannel.send(Resource.Failure(e.message))
			}
		}
	}

}