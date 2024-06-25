package com.batsworks.budget.ui.view_model.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.components.AJUST_TAG
import com.batsworks.budget.components.Resource
import com.batsworks.budget.domain.dto.UserDTO
import com.batsworks.budget.domain.dto.toDTO
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.entity.querySnapshotToEntity
import com.batsworks.budget.domain.repository.CustomRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
	private val validateName: ValidateName = ValidateName(),
	private val validatePhone: ValidatePhone = ValidatePhone(),
	private val validateEmail: ValidateEmail = ValidateEmail(),
	private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
	private val validatePassword: ValidatePassword = ValidatePassword(),
	private val validateTerms: ValidateTerms = ValidateTerms(),
	private val repository: CustomRepository<UserDTO>? = null,
) : ViewModel() {

	private val tag = LoginViewModel::class.java.name
	var state by mutableStateOf(RegistrationFormState())

	private val resourceEventChannel = Channel<Resource<Any>>()
	val resourceEventFlow = resourceEventChannel.receiveAsFlow()

	fun onEvent(event: RegistrationFormEvent) {
		when (event) {
			is RegistrationFormEvent.NameChanged -> {
				state = state.copy(nome = event.name)
			}

			is RegistrationFormEvent.EmailChanged -> {
				state = state.copy(email = event.email)
			}

			is RegistrationFormEvent.TelefoneChanged -> state =
				state.copy(telefone = event.telefone)

			is RegistrationFormEvent.PasswordChanged -> {
				state = state.copy(password = event.password)
				submitData()
			}

			is RegistrationFormEvent.RepeatedPasswordChanged -> {
				state = state.copy(repeatedPassword = event.repeatedPassword)
				submitData()
			}

			is RegistrationFormEvent.TermsChanged -> {
				state = state.copy(acceptedTerms = event.accepted)
			}

			is RegistrationFormEvent.Submit -> {
				registerUser()
			}
		}
	}

	private fun submitData() {
		val nameResult = validateName.execute(state.nome)
		val phoneResult = validatePhone.execute(state.telefone)
		val emailResult = validateEmail.execute(state.email)
		val passwordResult = validatePassword.execute(state.password)
		val repeatedPasswordResult =
			validateRepeatedPassword.execute(state.password, state.repeatedPassword)
		val termsResult = validateTerms.execute(state.acceptedTerms)

		val hasError = listOf(
			nameResult,
			emailResult,
			phoneResult,
			passwordResult,
			repeatedPasswordResult,
			termsResult
		).any { !it.successful }

		if (hasError) {
			state = state.copy(
				nomeError = nameResult.errorMessage,
				emailError = emailResult.errorMessage,
				telefoneError = phoneResult.errorMessage,
				passwordError = passwordResult.errorMessage,
				repeatedPasswordErro = repeatedPasswordResult.errorMessage,
				acceptedTermsError = termsResult.errorMessage
			)
			Log.d(AJUST_TAG(tag), "Foram encontrados erros")
			return
		}
	}

	private fun registerUser() {
		viewModelScope.launch {
			resourceEventChannel.send(Resource.Loading(true))
			if (repository == null) {
				resourceEventChannel.send(Resource.Failure("repository must not be null"))
				return@launch
			}
			val user = UserEntity(
				nome = state.nome,
				email = state.email,
				phone = state.telefone,
				password = state.password.toLong()
			)
			repository.findByParam("email", user.email)
				.addOnSuccessListener { documents ->
					viewModelScope.launch {
						val users = documents.toObjects(UserDTO::class.java)
						Log.d(AJUST_TAG(tag), "${users.size}")
						for (document in documents) {
							val userDTO = querySnapshotToEntity(document.data, document.id)
							if (userDTO.email == user.email) {
								resourceEventChannel.send(Resource.Loading(false))
								resourceEventChannel.send(Resource.Failure("Email already in use"))
							}
						}
						save(user)
						resourceEventChannel.send(Resource.Loading(false))
						resourceEventChannel.send(Resource.Sucess(""))
					}
				}.addOnFailureListener { e ->
					viewModelScope.launch {
						resourceEventChannel.send(Resource.Loading(false))
						resourceEventChannel.send(Resource.Failure(e.message))
					}
				}
		}
	}

	private fun save(user: UserEntity) = viewModelScope.launch {
		if (repository == null) {
			resourceEventChannel.send(Resource.Failure("repository must not be null"))
			return@launch
		}
		repository.save(toDTO(user)).addOnSuccessListener {
			Log.d(AJUST_TAG(tag), "usurio salvo")
			viewModelScope.launch {
				resourceEventChannel.send(Resource.Loading(false))
				resourceEventChannel.send(Resource.Sucess(""))
			}
		}.addOnFailureListener { e ->
			viewModelScope.launch {
				resourceEventChannel.send(Resource.Loading(false))
				resourceEventChannel.send(Resource.Failure(e.message))
			}
		}
	}

}