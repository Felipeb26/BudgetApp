package com.batsworks.budget.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.SwipeToDeleteContainer
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.notification.CustomToast
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.entity.isEntrance
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.CustomLottieAnimation
import com.batsworks.budget.ui.theme.customDarkBackground
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
            items(items = amounts.value, key = { it.id }) { amount ->
                SwipeToDeleteContainer(item = amount, onDelete = { am ->
                    model.deleteAmount(am.id)
                }) {
                    Content(amount, navController)
                }
            }
        }
    }
}


@Composable
private fun Content(amount: AmountEntity, navController: NavController) {
    val style = MaterialTheme.typography.labelLarge
    Row(
        Modifier
            .fillMaxWidth()
            .background(isEntrance(amount).copy(0.2f))
            .padding(10.dp, 15.dp, 15.dp, 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomText(text = amount.chargeName, isUpperCase = true, textStyle = style, wrap = true)
        CustomText(text = currency(amount.value), textStyle = style)
        if (amount.file != null) CustomButton(
            modifier = Modifier.height(35.dp),
            onClick = {
                easyNavigate(navController, Screen.ReceiptScreen.withArgs(amount.id.toString()))
            },
            text = "see file",
            enable = true
        )
    }
}