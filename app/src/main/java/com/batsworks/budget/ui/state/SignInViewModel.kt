package com.batsworks.budget.ui.state

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.domain.use_cases.ValidateEmail
import com.batsworks.budget.domain.use_cases.ValidatePassword
import com.batsworks.budget.ui.state.login.RegistrationFormEvent
import com.batsworks.budget.ui.state.login.RegistrationFormState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInViewModel(
	private val repository: CustomRepository<UserEntity>? = null,
	private val validateEmail: ValidateEmail = ValidateEmail(),
	private val validatePassword: ValidatePassword = ValidatePassword(),
) : ViewModel() {

	private val TAG = SignInViewModel::class.java.name

	private val validationEventChannel = Channel<ValidationEvent>()
	val validationEvents = validationEventChannel.receiveAsFlow()

	var state by mutableStateOf(RegistrationFormState())


	fun onEvent(event: RegistrationFormEvent) {
		when (event) {
			is RegistrationFormEvent.EmailChanged -> {
				state = state.copy(email = event.email)
			}

			is RegistrationFormEvent.PasswordChanged -> {
				state = state.copy(password = event.password)
			}

			is RegistrationFormEvent.Submit -> {
				signIn()
			}

			else -> throw Exception("Apenas permitido email se senha")
		}
	}

	private fun signIn() {
		val emailResult = validateEmail.execute(state.email)
		val passwordResult = validatePassword.execute(state.password)

		val hasError = listOf(emailResult, passwordResult).any { !it.successful }

		if (hasError) {
			state = state.copy(
				emailError = emailResult.errorMessage,
				passwordError = passwordResult.errorMessage
			)
			Log.d(TAG, "Erros foram localizados")
			return
		}
		viewModelScope.launch {
			if (repository == null) return@launch
			val all = repository.findAll().value
			all.forEach { Log.d(TAG, it.nome) }
			validationEventChannel.send(ValidationEvent.Sucess)
		}
	}

	fun test() = viewModelScope.launch {
		Log.d(TAG, "deucerto")
	}

	sealed class ValidationEvent {
		data object Sucess : ValidationEvent()
	}

}