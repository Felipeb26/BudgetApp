package com.batsworks.budget.ui.view_model.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.data.dao.UsersDAO
import com.batsworks.budget.data.entity.UserEntity
import com.batsworks.budget.services.connection.NetworkConnectivityObserver
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.ui.theme.custom.CustomTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val repository: UsersDAO,
	isConnected: NetworkConnectivityObserver,
	@ApplicationContext context: Context,
) : ViewModel() {

	private val _isReady = MutableStateFlow(false)
	val isReady = _isReady.asStateFlow()

	private val userEntityStateFlow = MutableStateFlow<UserEntity?>(null)
	val userStateFlow = userEntityStateFlow.asStateFlow()

	init {
		val notificationToast = NotificationToast(context)
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
	}

	fun changeTheme(customTheme: CustomTheme) {
		customTheme.findTheme(userStateFlow.value?.theme)
	}

}