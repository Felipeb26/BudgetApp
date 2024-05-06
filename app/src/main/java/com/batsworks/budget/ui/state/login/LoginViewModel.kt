package com.batsworks.budget.ui.state.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.domain.use_cases.ValidateEmail
import com.batsworks.budget.domain.use_cases.ValidateName
import com.batsworks.budget.domain.use_cases.ValidatePassword
import com.batsworks.budget.domain.use_cases.ValidateRepeatedPassword
import com.batsworks.budget.domain.use_cases.ValidateTerms
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
	private val validateName: ValidateName = ValidateName(),
	private val validateEmail: ValidateEmail = ValidateEmail(),
	private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
	private val validatePassword: ValidatePassword = ValidatePassword(),
	private val validateTerms: ValidateTerms = ValidateTerms(),
) : ViewModel() {

	private val TAG = LoginViewModel::class.simpleName!!
	var state by mutableStateOf(RegistrationFormState())

	private val validationEventChannel = Channel<ValidationEvent>()
	val validationEvents = validationEventChannel.receiveAsFlow()

	fun log() {
		Log.d(TAG, TAG.ifEmpty { "teste" })
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

			is RegistrationFormEvent.RepeatedPasswordChanged -> {
				state = state.copy(repeatedPassword = event.repeatedPassword)
			}

			is RegistrationFormEvent.TermsChanged -> {
				state = state.copy(acceptedTerms = event.accepted)
			}

			is RegistrationFormEvent.Submit -> {
				submitData()
			}
		}
	}

	private fun submitData() {
		val emailResult = validateEmail.execute(state.email)
		val passwordResult = validatePassword.execute(state.password)
		val repeatedPasswordResult =
			validateRepeatedPassword.execute(state.password, state.repeatedPassword)
		val nameResult = validateName.execute(state.nome)
		val termsResult = validateTerms.execute(state.acceptedTerms)

		val hasError = listOf(
			emailResult,
			passwordResult,
			repeatedPasswordResult,
			nameResult, termsResult
		).any { !it.successful }

		if (hasError) {
			state = state.copy(
				nomeError = nameResult.errorMessage,
				emailError = emailResult.errorMessage,
				passwordError = passwordResult.errorMessage,
				repeatedPasswordErro = repeatedPasswordResult.errorMessage,
				acceptedTermsError = termsResult.errorMessage
			)
			return
		}
		viewModelScope.launch {
			validationEventChannel.send(ValidationEvent.Sucess)
		}

	}

	sealed class ValidationEvent {
		object Sucess : ValidationEvent()
	}

}