package com.batsworks.budget.ui.objects

import com.batsworks.budget.R
import com.batsworks.budget.navigation.Screen

sealed class HomeCard(
	val name: String,
	val screen: Screen,
	val resource: Int,
) {
	data object Emprestimo :
		HomeCard("loans", Screen.PlusScreen, R.drawable.ic_account_balance)

	data object Cartoes :
		HomeCard("cards", Screen.PlusScreen, R.drawable.ic_add_card)

	data object Investimentos :
		HomeCard("investiments", Screen.PlusScreen, R.drawable.ic_monetization_on)
}