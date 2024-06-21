package com.batsworks.budget.ui.objects

import com.batsworks.budget.R
import com.batsworks.budget.navigation.Screen

sealed class HomeCard(
	val name: String,
	val screen: Screen,
	val resource: Int,
) {
	data object Emprestimo :
		HomeCard("loans", Screen.PlusScreen, R.drawable.baseline_account_balance_24)

	data object Cartoes :
		HomeCard("cards", Screen.PlusScreen, R.drawable.baseline_add_card_24)

	data object Investimentos :
		HomeCard("investiments", Screen.PlusScreen, R.drawable.baseline_monetization_on_24)
}