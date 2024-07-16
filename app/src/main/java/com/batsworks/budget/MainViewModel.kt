package com.batsworks.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

}