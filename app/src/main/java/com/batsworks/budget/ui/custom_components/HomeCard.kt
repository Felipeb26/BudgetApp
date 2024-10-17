package com.batsworks.budget.ui.custom_components

import android.content.Context
import androidx.compose.runtime.Composable
import com.batsworks.budget.R
import com.batsworks.budget.components.common.UiText
import com.batsworks.budget.navigation.Screen

sealed class HomeCard(
	private val nameRes: Int,
	val screen: Screen,
	val resource: Int,
) {

	fun getName(context: Context) = UiText.StringResource(nameRes).asString(context)
	@Composable
	fun getName() = UiText.StringResource(nameRes).asString()

	data object Emprestimo : HomeCard(
		R.string.loans,
		Screen.PlusScreen,
		R.drawable.ic_account_balance
	)

	data object Cartoes : HomeCard(
		R.string.cards,
		Screen.PlusScreen,
		R.drawable.ic_add_card
	)

	data object Investimentos : HomeCard(
		R.string.investiments,
		Screen.PlusScreen,
		R.drawable.ic_monetization_on
	)
}