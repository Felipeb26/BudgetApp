package com.batsworks.budget

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.services.worker.SyncData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration

class MainViewModel(private val repository: UsersDao = BudgetApplication.database.getUsersDao()) :
	ViewModel() {


	private val _isReady = MutableStateFlow(false)
	val isReady = _isReady.asStateFlow()

	private val userEntityStateFlow = MutableStateFlow<UserEntity?>(null)
	val userEntity = userEntityStateFlow.asStateFlow()

	init {
		viewModelScope.launch {
			delay(1000)
			_isReady.emit(true)
		}
		viewModelScope.launch {
			userEntityStateFlow.emit(repository.findUser())
		}
	}

	fun syncData(context: Context) {
		val workerRequest = PeriodicWorkRequestBuilder<SyncData>(Duration.ofSeconds(75)).build()
		WorkManager.getInstance(context).enqueue(workerRequest)
	}

}