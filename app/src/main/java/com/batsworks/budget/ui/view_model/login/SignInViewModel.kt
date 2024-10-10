package com.batsworks.budget.ui.view_model.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.components.AJUST_TAG
import com.batsworks.budget.components.Resource
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.entity.UserFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.use_cases.user.ValidateEmail
import com.batsworks.budget.use_cases.user.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val repository: CustomRepository<UserEntity>,
	private val localRepository: UsersDao,
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

			else -> throw Exception("Apenas permitido email e senha")
		}
	}

	private fun signIn() {
		try {
			val emailResult = validateEmail.execute(state.email)
			val passwordResult = validatePassword.execute(state.password)

			val hasError = listOf(emailResult, passwordResult).any { !it.successful }
			if (hasError) {
				state = state.copy(
					emailError = emailResult.errorMessage,
					passwordError = passwordResult.errorMessage
				)
				Log.d(AJUST_TAG(tag), "Erros foram localizados")
				return
			}
			viewModelScope.launch {
				validationEventChannel.send(Resource.Loading())

				var user = localRepository.findByLogin(state.email, state.password.toInt())

				if (user == null) {
					val map = HashMap<String, Any>()
					map["email"] = state.email
					map["password"] = state.password.toLong()

					repository.findByLogin(map)
						.addOnSuccessListener { documents ->
							if (documents.size() <= 0) {
								viewModelScope.launch {
									validationEventChannel.send(Resource.Loading(false))
									validationEventChannel.send(Resource.Failure("No user found, check your credentials!"))
								}
							}
							Log.d(AJUST_TAG(tag), "Encontrou ${documents.size()} usuarios assim ")
							for (document in documents) {
								viewModelScope.launch {
									val userDTO = document.toObject(UserFirebaseEntity::class.java)
									if (userDTO.email == state.email) {
										user = userDTO.toEntity()
										localRepository.save(user!!)
										validationEventChannel.send(Resource.Loading(false))
										validationEventChannel.send(Resource.Sucess(""))
									}
								}
							}
						}.addOnFailureListener { exception ->
							Log.d(AJUST_TAG(tag), exception.message ?: "error has happen")
							viewModelScope.launch {
								validationEventChannel.send(Resource.Loading(false))
								validationEventChannel.send(Resource.Failure(exception.message))
							}
						}
				} else {
					user?.loginWhenEnter.let { localRepository.save(user!!.withLoginWhenEnter(true)) }
					validationEventChannel.send(Resource.Loading(false))
					validationEventChannel.send(Resource.Sucess(""))
				}
			}
		} catch (e: Exception) {
			Log.e(AJUST_TAG(tag), e.message ?: "un error has happen")
		}
	}
}