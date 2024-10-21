package com.batsworks.budget.ui.view_model.login

data class RegistrationFormState(
	val nome: String = "",
	val nomeError: String? = null,
	val email: String = "",
	val emailError: String? = null,
	val telefone: String = "",
	val telefoneError: String? = null,
	val password: String = "",
	val passwordError: String? = null,
	val repeatedPassword: String = "",
	val repeatedPasswordErro: String? = null,
	val acceptedTerms: Boolean = false,
	val loginOnEnter: Boolean = false,
	val acceptedTermsError: String? = null,
)
