package com.batsworks.budget.ui.view_model.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.ui.components.menu.AJUST_TAG
import com.batsworks.budget.data.dao.UsersDao
import com.batsworks.budget.data.entity.UserEntity
import com.batsworks.budget.domain.Resource
import com.batsworks.budget.ui.view_model.login.RegistrationFormEvent
import com.batsworks.budget.ui.view_model.login.RegistrationFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val localRepository: UsersDao) : ViewModel() {

	private val tag = ProfileViewModel::class.java.name
	var state by mutableStateOf(RegistrationFormState())

	private val resourceEventChannel = Channel<Resource<Any>>()
	val resourceEventFlow = resourceEventChannel.receiveAsFlow()

	private val userEntityStateFlow = MutableStateFlow<UserEntity?>(null)
	val userEntity = userEntityStateFlow.asStateFlow()

	init {
		viewModelScope.launch {
			userEntityStateFlow.emit(localRepository.findUser())
		}
	}

	fun dontLoginWhenStart() {
		viewModelScope.launch {
			var user: UserEntity = localRepository.findUser()
			user = user.withLoginWhenEnter(false)
			localRepository.save(user)
		}
	}

	fun onEvent(event: RegistrationFormEvent) {
		when (event) {
			is RegistrationFormEvent.NameChanged -> {
				state = state.copy(nome = event.name)
			}

			is RegistrationFormEvent.EmailChanged -> {
				state = state.copy(email = event.email)
			}

			is RegistrationFormEvent.TelefoneChanged -> {
				state = state.copy(telefone = event.telefone)
			}

			is RegistrationFormEvent.PasswordChanged -> {
				state = state.copy(password = event.password)
			}

			is RegistrationFormEvent.LoginOnEnterChanged ->{
				state = state.copy(loginOnEnter = event.login)
			}

			is RegistrationFormEvent.Submit -> submit()
			else -> Unit
		}
	}

	private fun submit() {
		viewModelScope.launch {
			try {
				resourceEventChannel.send(Resource.Loading(true))
				var user: UserEntity = localRepository.findUser()

				user = user.withName(state.nome)
					.withEmail(state.email)
					.withPhone(state.telefone)
					.withPassword(state.password)
					.withLoginWhenEnter(state.loginOnEnter)
				localRepository.save(user)
				resourceEventChannel.send(Resource.Loading(false))
				resourceEventChannel.send(Resource.Sucess(true))
			} catch (e: Exception) {
				Log.e(AJUST_TAG(tag), e.message ?: "erro found")
				resourceEventChannel.send(Resource.Loading(false))
				resourceEventChannel.send(Resource.Failure(e.message))
			}
		}
	}



}