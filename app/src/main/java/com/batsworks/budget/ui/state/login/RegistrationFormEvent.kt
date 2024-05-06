package com.batsworks.budget.ui.state.login

sealed class RegistrationFormEvent {
	data class NameChanged(val name: String) : RegistrationFormEvent()
	data class EmailChanged(val email: String) : RegistrationFormEvent()
	data class TelefoneChanged(val telefone: String) : RegistrationFormEvent()
	data class PasswordChanged(val password: String) : RegistrationFormEvent()
	data class RepeatedPasswordChanged(val repeatedPassword: String) : RegistrationFormEvent()
	data class TermsChanged(val accepted: Boolean) : RegistrationFormEvent()

	object Submit : RegistrationFormEvent()
}
