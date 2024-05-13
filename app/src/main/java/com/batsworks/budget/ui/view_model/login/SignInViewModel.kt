package com.batsworks.budget.ui.view_model.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.ajustTag
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.entity.querySnapshotToEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.domain.use_cases.login.ValidateEmail
import com.batsworks.budget.domain.use_cases.login.ValidatePassword
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInViewModel(
	private val repository: CustomRepository<UserEntity>? = null,
	private val localRepository: UsersDao = BudgetApplication.database.getUsersDao(),
	private val validateEmail: ValidateEmail = ValidateEmail(),
	private val validatePassword: ValidatePassword = ValidatePassword(),
) : ViewModel() {


	private val tag = SignInViewModel::class.java.name

	private val validationEventChannel = Channel<Resource<Any>>()
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

			is RegistrationFormEvent.TermsChanged -> {
				state = state.copy(acceptedTerms = event.accepted)
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
			Log.d(ajustTag(tag), "Erros foram localizados")
			return
		}
		viewModelScope.launch {
			if (repository == null) return@launch
			validationEventChannel.send(Resource.Loading())


			val user = localRepository.findByLogin(state.email, state.password.toInt())

			if (user == null) {
				repository.findByParam(
					Filter.equalTo("email", state.email),
					Filter.equalTo("password", state.password.toLong())
				)
					.addOnSuccessListener { documents ->
						for (document in documents) {
							viewModelScope.launch {
								val userDTO = querySnapshotToEntity(document.data, document.id)
								if (userDTO.email == state.email) {
									localRepository.save(
										querySnapshotToEntity(
											document.data,
											document.id,
											state.acceptedTerms
										)
									)
									validationEventChannel.send(Resource.Loading(false))
									validationEventChannel.send(Resource.Sucess(""))
								}
							}
						}
					}.addOnFailureListener { exception ->
						viewModelScope.launch {
							validationEventChannel.send(Resource.Loading(false))
							validationEventChannel.send(
								Resource.Failure(
									exception.message ?: "An error has happen"
								)
							)
						}
					}
			} else {
				if(user.loginWhenEnter != state.acceptedTerms){
					localRepository.save(user.withLoginWhenEnter(state.acceptedTerms))
				}

				validationEventChannel.send(Resource.Loading(false))
				validationEventChannel.send(Resource.Sucess(""))
			}
		}
	}

}