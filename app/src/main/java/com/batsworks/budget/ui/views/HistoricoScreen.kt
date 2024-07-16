package com.batsworks.budget.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.DropDownMenu
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.animations.CustomLottieAnimation
import com.batsworks.budget.components.animations.SwitchElementsView
import com.batsworks.budget.components.buttons.CustomIconButton
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.functions.PullToRefreshLazyColumn
import com.batsworks.budget.components.functions.SwipeToDeleteContainer
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.entity.isEntrance
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.textColor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration

@Composable
fun Historico(
	navController: NavController,
	resourceEventFlow: Flow<Resource<Any>>,
	amounts: List<AmountEntity>,
	setAmountList: (List<AmountEntity>) -> Unit,
	deleteAmount: (Int) -> Unit,
	searchAmount: () -> Unit,
) {
	val coroutine = rememberCoroutineScope()
	val context = LocalContext.current
	val toast = NotificationToast(context)

	LaunchedEffect(key1 = context) {
		resourceEventFlow.collect { event ->
			when (event) {
				is Resource.Loading -> {}

				is Resource.Sucess -> {
					toast.show("item deletado com sucesso")
				}

				is Resource.Failure -> {
					toast.show(event.error ?: "Não foi possivel deletar o item")
				}
			}
		}
	}

	val (moneyFlow, setMoneyFlow) = remember { mutableStateOf("") }
	var isRefreshing by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(if (amounts.isEmpty()) Color800.copy(0.6f) else customBackground)
	) {
		CustomFilter(setAmountList, moneyFlow, setMoneyFlow)
		SwitchElementsView(start = amounts.isEmpty(), content = {
			CustomLottieAnimation(R.raw.empty, amounts.isEmpty())
			CustomText(
				modifier = Modifier
					.padding(vertical = 20.dp)
					.fillMaxWidth(),
				textStyle = MaterialTheme.typography.titleMedium,
				text = stringResource(id = R.string.empty_transaction),
				textAlign = TextAlign.Center, textWeight = FontWeight.Bold,
				isUpperCase = true
			)
		}) {
			PullToRefreshLazyColumn(itens = amounts,
				isRefreshing = isRefreshing,
				onRefresh = {
					isRefreshing = true
					searchAmount()
					coroutine.launch {
						delay(Duration.ofMillis(2500))
						isRefreshing = !isRefreshing
					}
				},
				content = { amount ->
					SwipeToDeleteContainer(item = amount,
						onDelete = { am -> deleteAmount(am.id) }) {
						Content(amount, navController)
					}
				}
			)
		}
	}
}


@Composable
private fun CustomFilter(
	setAmountList: (List<AmountEntity>) -> Unit,
	moneyFlow: String, selectMoneyFlow: (String) -> Unit,
) {
	var expandedEntrance by remember { mutableStateOf(false) }
	var expanded by remember { mutableStateOf(false) }

	Row(Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 20.dp)) {
		DropDownMenu(
			modifier = Modifier.weight(1f),
			itens = listOf("entrada", "saida"),
			onExpandChage = { expandedEntrance = !expandedEntrance },
			onDismiss = { expandedEntrance = !expandedEntrance },
			expanded = expandedEntrance, selectText = moneyFlow,
			onValueChange = {
				selectMoneyFlow(it)
				setAmountList.invoke(emptyList())
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
		Spacer(modifier = Modifier.height(10.dp))
		Row(
			Modifier
				.fillMaxWidth()
				.border(2.dp, color = backgroundColor.copy(0.6f), RoundedCornerShape(10))
				.background(backgroundColor.copy(0.4f))
				.padding(10.dp, 15.dp, 15.dp, 10.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			CustomText(
				text = amount.chargeName, capitalize = true,
				textStyle = style.copy(letterSpacing = TextUnit(0.5f, TextUnitType.Sp)),
				textWeight = FontWeight.Bold, wrap = true
			)
			CustomText(
				text = currency(amount.value),
				textStyle = style,
				textWeight = FontWeight.Bold
			)
			if (amount.file != null) {
				CustomIconButton(contentColor = backgroundColor.copy(0.8f),
					containerColor = textColor.copy(0.4f),
					imageVector = ImageVector.vectorResource(id = R.drawable.baseline_visibility_24),
					onClick = {
						easyNavigate(
							navController,
							Screen.ReceiptScreen.withArgs(amount.id.toString())
						)
					})
			}
		}
		Spacer(modifier = Modifier.height(10.dp))
	}
}

@PreviewLightDark
@Composable
fun PreviewHistorico() {
	val resource = Channel<Resource<Any>>()
	val amounts = listOf(
		AmountEntity(
			chargeName = "conta de luz",
			entrance = true,
			file = "teste.".encodeToByteArray()
		)
	)
	Historico(navController = rememberNavController(), resource.receiveAsFlow(), amounts, {}, {}) {}
}