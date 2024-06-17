package com.batsworks.budget.ui.views

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.composeBool
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.dataToString
import com.batsworks.budget.components.notification.NotificationSnackBar
import com.batsworks.budget.components.notification.Notifications
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Loading
import com.batsworks.budget.ui.theme.customDarkBackground
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun ReceiptScreen(
    entityAmount: StateFlow<AmountEntity?>,
    resourceEventFlow: Flow<Resource<Any>>,
    downloadReceipt: (AmountEntity) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val notifications = Notifications(context)
    val coroutine = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val (isLoading, setLoading) = mutableStateOf(false)
    val snackBar = NotificationSnackBar(coroutine, snackBarHostState)

    LaunchedEffect(key1 = context) {
        resourceEventFlow.collect { event ->
            when (event) {
                is Resource.Loading -> {
                    setLoading(isLoading)
                    snackBar.show("Downloading Image")
                }

                is Resource.Failure -> snackBar.show("Error while downloading: ${event.error}")

                is Resource.Sucess -> {
                    val values = event.result.toString().split("|")
                    notifications.showBasicNotification(
                        text = "Download complete of ${values[1]}",
                        filePath = values[0]
                    )
                }
            }
        }
    }
    val amount = entityAmount.collectAsState()

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackBarHostState, snackbar = {
            Snackbar(
                snackbarData = it,
                containerColor = composeBool(isSystemInDarkTheme(), Color500, Color700),
                contentColor = Color50, actionColor = Color50, dismissActionContentColor = Color50
            )
        })
    }) { padding ->
        Column(
            modifier = Modifier
                .background(customDarkBackground)
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            AmountInfo(amount)
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .height((configuration.screenHeightDp / 2).dp)
                    .padding(horizontal = 20.dp),
                model = amount.value?.file,
                contentDescription = amount.value?.chargeName,
            )

            Spacer(modifier = Modifier.height(35.dp))
            ActionButtons(amount, downloadReceipt)
        }
    }

    Loading(isLoading)
}

@Composable
private fun AmountInfo(amount: State<AmountEntity?>) {
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        CustomText(
            textStyle = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center, capitalize = true,
            textDecoration = TextDecoration.Underline,
            text = stringResource(id = R.string.bill_name).plus(amount.value?.chargeName),
        )
        CustomText(
            textStyle = MaterialTheme.typography.titleMedium,
            text = stringResource(id = R.string.bill_value).plus(currency(amount.value?.value))
        )
    }

    CustomText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(horizontal = 40.dp),
        textAlign = TextAlign.Start, capitalize = true,
        textStyle = MaterialTheme.typography.titleMedium,
        text = stringResource(id = R.string.bill_date).plus(dataToString(amount.value?.amountDate))
    )
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun ActionButtons(amount: State<AmountEntity?>, downloadReceipt: (AmountEntity) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(start = 15.dp, end = 15.dp, bottom = 20.dp),
    ) {
        CustomButton(
            modifier = Modifier.weight(1f),
            onClick = { amount.value?.let { downloadReceipt(it) } },
            enable = true, text = stringResource(id = R.string.download)
        )
        Spacer(modifier = Modifier.width(10.dp))
        CustomButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ })
    }
}

@Composable
@PreviewLightDark
fun ReceiptScreenPreview() {
    val resource = Channel<Resource<Any>>()
    val amountEntity =
        remember { MutableStateFlow<AmountEntity?>(AmountEntity(chargeName = "teste")) }

    ReceiptScreen(amountEntity, resource.receiveAsFlow()) {}
}