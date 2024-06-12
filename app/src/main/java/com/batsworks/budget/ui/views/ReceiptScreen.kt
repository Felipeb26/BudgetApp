package com.batsworks.budget.ui.views

import androidx.compose.foundation.background
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.dataToString
import com.batsworks.budget.components.notification.CustomSnackBar
import com.batsworks.budget.components.notification.Notifications
import com.batsworks.budget.ui.theme.Loading
import com.batsworks.budget.ui.theme.customDarkBackground
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.receipt.ReceiptViewModel

@Composable
fun ReceiptScreen(navController: NavController, id: String) {
	val context = LocalContext.current
	val notifications = Notifications(context)
	val coroutine = rememberCoroutineScope()
	val snackBarHostState = remember { SnackbarHostState() }
	val (isLoading, setLoading) = mutableStateOf(false)

	val model = viewModel<ReceiptViewModel>(
		factory = factoryProvider(ReceiptViewModel(context = context, id = id))
	)
	LaunchedEffect(key1 = context) {
		model.resourceEventFlow.collect { event ->
			when (event) {
				is Resource.Loading -> {
					setLoading(isLoading)
					CustomSnackBar(coroutine, snackBarHostState, "Downloading Image")
				}

				is Resource.Failure -> {
					CustomSnackBar(
						coroutine,
						snackBarHostState,
						"Error while downloading: ${event.error}"
					)
				}

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
	val amount = model.entityAmount.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customDarkBackground),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(15.dp)
	) {
		SnackbarHost(hostState = snackBarHostState)
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
				.padding(horizontal = 20.dp),
			textAlign = TextAlign.Start,
			textStyle = MaterialTheme.typography.titleMedium,
			text = stringResource(id = R.string.bill_date).plus(dataToString(amount.value?.amountDate))
		)
		Spacer(modifier = Modifier.height(20.dp))
		AsyncImage(
			modifier = Modifier
				.fillMaxWidth()
				.padding(0.dp)
				.padding(horizontal = 20.dp),
			model = amount.value?.file,
			contentDescription = amount.value?.chargeName
		)

		Spacer(modifier = Modifier.height(35.dp))
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(0.dp)
				.padding(horizontal = 15.dp),
		) {
			CustomButton(
				modifier = Modifier.weight(1f),
				onClick = { amount.value?.let { model.downloadImage(it) } },
				enable = true, text = stringResource(id = R.string.download)
			)
			Spacer(modifier = Modifier.width(10.dp))
			CustomButton(
				modifier = Modifier.weight(1f),
				onClick = { /*TODO*/ })
		}
	}

	Loading(isLoading)
}

@Composable
@PreviewLightDark
fun ReceiptScreenPreview() {
	ReceiptScreen(rememberNavController(), "0")
}
