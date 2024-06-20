package com.batsworks.budget.ui.objects

import androidx.compose.ui.graphics.Color
import com.batsworks.budget.R
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.theme.ColorCardCartoes
import com.batsworks.budget.ui.theme.ColorCardEmprestimo
import com.batsworks.budget.ui.theme.ColorCardInvestimentos

sealed class HomeCard(
	val name: String,
	val color: Color,
	val screen: Screen,
	val resource: Int,
) {
	data object Emprestimo :
		HomeCard("loans", ColorCardEmprestimo, Screen.PlusScreen, R.drawable.baseline_account_balance_24)

	data object Cartoes :
		HomeCard("cards", ColorCardCartoes, Screen.PlusScreen, R.drawable.baseline_add_card_24)

	data object Investimentos :
		HomeCard(
			"investiments",
			ColorCardInvestimentos,
			Screen.PlusScreen,
			R.drawable.baseline_monetization_on_24
		)
}