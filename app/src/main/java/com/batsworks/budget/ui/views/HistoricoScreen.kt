package com.batsworks.budget.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomIconButton
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.DropDownMenu
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.SwipeToDeleteContainer
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.notification.CustomToast
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.entity.isEntrance
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.CustomLottieAnimation
import com.batsworks.budget.ui.theme.customDarkBackground
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.history.HistoryViewModel

@Composable
fun Historico(
	navController: NavController,
	model: HistoryViewModel = viewModel<HistoryViewModel>(),
) {
	model.init()
	val context = LocalContext.current
	val amounts = model.amounts

	LaunchedEffect(key1 = context) {
		model.resourceEventFlow.collect { event ->
			when (event) {
				is Resource.Loading -> {}

				is Resource.Sucess -> {
					CustomToast(context, "item deletado com sucesso")
				}

				is Resource.Failure -> {
					CustomToast(context, event.error ?: "Não foi possivel deletar o item")
				}
			}
		}
	}

	if (amounts.value.isEmpty()) {
		CustomLottieAnimation(R.raw.empty, amounts.value.isEmpty())
		CustomText(
			modifier = Modifier
				.padding(vertical = 20.dp)
				.fillMaxWidth(),
			textStyle = MaterialTheme.typography.titleMedium,
			text = "Sem nenhum conteudo",
			textAlign = TextAlign.Center,
			textWeight = FontWeight.Bold,
			isUpperCase = true
		)
	} else {
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.background(customDarkBackground)
		) {
			item { CustomFilter(model) }
			items(items = amounts.value, key = { it.id }) { amount ->
				SwipeToDeleteContainer(
					item = amount,
					onDelete = { am -> model.deleteAmount(am.id) }) {
					Content(amount, navController)
				}
			}
		}
	}
}


@Composable
private fun CustomFilter(model: HistoryViewModel) {
	var expandedEntrance by remember { mutableStateOf(false) }
	var selectedEntrance by remember { mutableStateOf("") }
	var expanded by remember { mutableStateOf(false) }

	Row(Modifier.padding(15.dp)) {
		DropDownMenu(
			modifier = Modifier.weight(1f),
			itens = listOf("entrada", "saida"),
			onExpandChage = { expandedEntrance = !expandedEntrance },
			onDismiss = { expandedEntrance = !expandedEntrance },
			expanded = expandedEntrance, selectText = selectedEntrance,
			onValueChange = {
				selectedEntrance = it
				model.findByEntrace(selectedEntrance)
			}
		)
		Spacer(modifier = Modifier.width(10.dp))
		DropDownMenu(
			modifier = Modifier.weight(1f),
			onExpandChage = { expanded = !expanded },
			onDismiss = { expanded = !expanded },
			expanded = expanded, itens = listOf(""),
			onValueChange = {}
		)
		Spacer(modifier = Modifier.width(10.dp))
		DropDownMenu(
			modifier = Modifier.weight(1f),
			onExpandChage = { expanded = !expanded },
			onDismiss = { expanded = !expanded },
			expanded = expanded, itens = listOf(""),
			onValueChange = {}
		)
	}
}

@Composable
private fun Content(amount: AmountEntity, navController: NavController) {
	val style = MaterialTheme.typography.labelLarge
	val backgroundColor = isEntrance(amount)

	Column(modifier = Modifier.padding(10.dp, 0.dp)) {
		Spacer(
			modifier = Modifier
				.height(10.dp)
				.background(Color50)
		)
		Row(
			Modifier
				.fillMaxWidth()
				.background(backgroundColor.copy(0.4f))
				.border(2.dp, color = backgroundColor.copy(0.6f), RoundedCornerShape(15))
				.padding(10.dp, 15.dp, 15.dp, 10.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			CustomText(
				text = amount.chargeName, isUpperCase = true,
				textStyle = style.copy(letterSpacing = TextUnit(0.5f, TextUnitType.Sp)),
				textWeight = FontWeight.Bold, wrap = true
			)
			CustomText(
				text = currency(amount.value),
				textStyle = style,
				textWeight = FontWeight.Bold
			)
			if (amount.file != null) CustomIconButton(contentColor = backgroundColor.copy(0.8f),
				imageVector = ImageVector.vectorResource(id = R.drawable.baseline_visibility_24),
				onClick = {
					easyNavigate(navController, Screen.ReceiptScreen.withArgs(amount.id.toString()))
				})
		}
		Spacer(
			modifier = Modifier
				.height(10.dp)
				.background(Color50)
		)
	}
}

@PreviewLightDark
@Composable
fun PreviewHistorico() {
	val model = viewModel<HistoryViewModel>(
		factory = factoryProvider(
			HistoryViewModel(
				BudgetApplication.database.getAmountDao()
			)
		)
	)
	Historico(navController = rememberNavController(), model)
}