package com.batsworks.budget.ui.objects

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.batsworks.budget.R

sealed class HomeCard(
	val name: String,
	val color: Color,
	val icon: ImageVector? = null,
	val resource: Int = 0,
) {
	object Emprestimo :
		HomeCard("emprestimo", Color.Red, null, R.drawable.baseline_account_balance_24)

	object Cartoes :
		HomeCard("cards", Color.Magenta, null, R.drawable.baseline_add_card_24)

	object Investimentos :
		HomeCard("investimentos", Color.Cyan, null, R.drawable.baseline_monetization_on_24)
}