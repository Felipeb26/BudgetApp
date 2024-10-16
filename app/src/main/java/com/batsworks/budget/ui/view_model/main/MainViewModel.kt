package com.batsworks.budget.ui.view_model.main

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.services.connection.NetworkConnectivityObserver
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.ui.theme.CustomTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val repository: UsersDao,
	private val customTheme: CustomTheme,
	isConnected: NetworkConnectivityObserver,
	private val notificationToast: NotificationToast,
) : ViewModel() {

	private val _isReady = MutableStateFlow(false)
	val isReady = _isReady.asStateFlow()

	private val userEntityStateFlow = MutableStateFlow<UserEntity?>(null)
	val userEntity = userEntityStateFlow.asStateFlow()

	init {
		runBlocking {
			userEntityStateFlow.emit(repository.findUser())
		}
		viewModelScope.launch {
			delay(1000)
			_isReady.emit(true)
		}

		isConnected.observeForever {
			Log.d("WIFI-CONECT", it.toString())
			notificationToast.show(if (it) "ligado" else "desligado", Toast.LENGTH_LONG)
		}
		customTheme.setTheme(userEntity.value?.theme)
	}

}