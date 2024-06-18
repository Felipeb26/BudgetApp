package com.batsworks.budget.ui.objects

import androidx.compose.ui.graphics.Color
import com.batsworks.budget.R
import com.batsworks.budget.navigation.Screen

sealed class HomeCard(
	val name: String,
	val color: Color,
	val screen: Screen,
	val resource: Int,
) {
	data object Emprestimo :
		HomeCard("loans", Color.Red, Screen.PlusScreen, R.drawable.baseline_account_balance_24)

	data object Cartoes :
		HomeCard("cards", Color.Yellow, Screen.PlusScreen, R.drawable.baseline_add_card_24)

	data object Investimentos :
		HomeCard(
			"investiments",
			Color.Green,
			Screen.PlusScreen,
			R.drawable.baseline_monetization_on_24
		)
}