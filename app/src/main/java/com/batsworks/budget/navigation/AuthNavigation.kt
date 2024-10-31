package com.batsworks.budget.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.batsworks.budget.ui.view_model.login.LoginViewModel
import com.batsworks.budget.ui.view_model.login.SignInViewModel
import com.batsworks.budget.ui.views.Login
import com.batsworks.budget.ui.views.SignUp

fun NavGraphBuilder.authNavigation(navController: NavController) {
	composable<Screen.LoginScreen> {
		val model = hiltViewModel<SignInViewModel>()
		Login(navController, model.state, model.validationEvents, model::onEvent)
	}

	composable<Screen.SignUpScreen> {
		val model = hiltViewModel<LoginViewModel>()
		SignUp(navController, model)
	}
}