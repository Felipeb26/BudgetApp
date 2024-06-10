package com.batsworks.budget.ui.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.notification.CustomToast
import com.batsworks.budget.components.notification.Notifications
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Loading
import com.batsworks.budget.ui.theme.customDarkBackground
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.receipt.ReceiptViewModel

@Composable
fun ReceiptScreen(navController: NavController, id: String) {
	val configuration = LocalConfiguration.current
	val context = LocalContext.current
	val notifications = Notifications(context)
	val (isLoading, setLoading) = mutableStateOf(false)
	val snackBarHostState = remember { SnackbarHostState() }

	var test by remember { mutableStateOf("/sdcard/Receipt_11May2024_001432.pdf") }

	val model = viewModel<ReceiptViewModel>(
		factory = factoryProvider(
			ReceiptViewModel(
				context = context,
				id = id
			)
		)
	)

	LaunchedEffect(key1 = context) {
		model.resourceEventFlow.collect { event ->
			when (event) {
				is Resource.Loading -> {
					setLoading(!isLoading)
					CustomToast(context, "Downloading . . .")
				}

				is Resource.Failure -> CustomToast(
					context,
					event.error ?: "houve um erro ao criar o usuario"
				)

				is Resource.Sucess -> {
					val values = event.result.toString().split("|")

					test = values[0]
					notifications.showBasicNotification(
						text = "Download complete of ${values[1]}",
						filePath = values[0]
					)
				}
			}
		}
	}
	val amount = model.entityAmount.collectAsState()
	var activity = LocalContext.current as Activity
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
			horizontalArrangement = Arrangement.SpaceAround
		) {
			CustomText(
				textStyle = MaterialTheme.typography.titleMedium,
				isUpperCase = true, text = amount.value?.chargeName ?: "",
				textAlign = TextAlign.Center, textDecoration = TextDecoration.Underline
			)
			CustomText(
				modifier = Modifier.clickable {
					val intent = Intent(Intent.ACTION_VIEW, Uri.parse(test))
					intent.setDataAndType( Uri.parse(test),"*/*")
					activity.startActivity(intent)
				},
				textStyle = MaterialTheme.typography.titleMedium,
				text = test
			)
		}

		AsyncImage(
			modifier = Modifier
				.fillMaxWidth()
				.border(0.1.dp, Color400, shape = RoundedCornerShape(5))
				.height((configuration.screenHeightDp / 1.5).dp),
			model = amount.value?.file,
			contentDescription = amount.value?.chargeName
		)
//        CustomSnackBar(coroutine, snackBarHostState, "")
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			CustomButton(
				onClick = { amount.value?.let { model.downloadImage(it) } },
				enable = true, text = "baixar"
			)
			CustomButton(onClick = { /*TODO*/ })
		}
	}

	Loading(isLoading)
}

@Composable
@PreviewLightDark
fun ReceiptScreenPreview() {
	ReceiptScreen(rememberNavController(), "0")
}
