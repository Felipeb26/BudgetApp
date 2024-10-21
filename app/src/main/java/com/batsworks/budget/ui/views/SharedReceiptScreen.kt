package com.batsworks.budget.ui.views

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.batsworks.budget.R
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.formatter.localDate
import com.batsworks.budget.components.visual_transformation.CurrencyTransformation
import com.batsworks.budget.domain.Resource
import com.batsworks.budget.navigation.Navigate
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.formatNavigation
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.ui.components.buttons.CustomButton
import com.batsworks.budget.ui.components.buttons.CustomCheckBox
import com.batsworks.budget.ui.components.texts.CustomText
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.add.AmountFormEvent
import com.batsworks.budget.ui.view_model.add.AmountFormState
import com.batsworks.budget.utils.files.image.getByteArrayFromUri
import com.batsworks.budget.utils.files.pdf.ComposePDFViewer
import com.batsworks.budget.utils.files.zip.decompressData
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.time.LocalDate

@Composable
fun SharedReceipt(
	file: Uri,
	type: String,
	resourceEventFlow: Flow<Resource<Any>>,
	state: AmountFormState,
	onEvent: (AmountFormEvent) -> Unit,
) {
	Log.d("VALUE", file.toString())
	val context = LocalContext.current
	var loading by remember { mutableStateOf(false) }
	var sucess by remember { mutableStateOf(false) }
	val toast = NotificationToast(context)

	val exchanges = arrayOf("entrance", "output")
	val entrance = remember { mutableStateOf(false) }
	var uri by remember { mutableStateOf(file) }
	var billName by remember { mutableStateOf("") }
	var billCost by remember { mutableStateOf("") }

	LaunchedEffect(Unit) {
		delay(Duration.ofMillis(1500))
		onEvent(AmountFormEvent.FileVoucher(getByteArrayFromUri(context, uri)))
	}

	LaunchedEffect(context) {
		resourceEventFlow.collect { event ->
			when (event) {
				is Resource.Loading -> {
					loading = event.loading
					toast.show(context.getString(R.string.loading))
				}

				is Resource.Failure -> {
					toast.show(event.error ?: context.getString(R.string.adding_bill_error))
				}

				is Resource.Sucess -> {
					toast.show(context.getString(R.string.adding_bill_sucess))
					sucess = !sucess
				}
			}
		}
	}
	sucess.apply { Navigate(screen = formatNavigation(Screen.LoginScreen.route)) }

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.padding(0.dp)
			.padding(top = 30.dp, start = 15.dp, end = 15.dp),
		horizontalAlignment = Alignment.Start
	) {
		item {
			Spacer(modifier = Modifier.height(15.dp))
			CustomOutlineTextField(
				modifier = Modifier.fillMaxWidth(0.95f),
				labelText = stringResource(id = R.string.bill_name),
				text = billName, onValueChange = {
					billName = it
					onEvent(AmountFormEvent.ChargeNameEventChange(it))
				}, error = state.chargeNameError != null,
				errorMessage = state.chargeNameError
			)
			CustomOutlineTextField(
				modifier = Modifier.fillMaxWidth(0.95f),
				transformation = CurrencyTransformation(),
				labelText = stringResource(id = R.string.bill_value),
				text = billCost, onValueChange = {
					billCost = it
					if (it.isNotBlank()) onEvent(AmountFormEvent.ValueEventChange(it))
				}, error = state.valueError != null,
				errorMessage = state.valueError
			)
		}
		item { CalendarPick(onEvent, state) }
		item {
			Row {
				exchanges.forEachIndexed { index, exchange ->
					if ((index % 2) == 0) {
						EntranceButton(
							modifier = Modifier.weight(1f),
							exchange = exchange,
							entrance.value, entrance, onEvent, state
						)
					} else EntranceButton(
						modifier = Modifier.weight(1f),
						exchange = exchange,
						!entrance.value, entrance, onEvent, state
					)
				}
			}
		}
		item {
			CustomButton(
				modifier = Modifier.fillMaxWidth(),
				enable = true,
				onClick = {
					onEvent(AmountFormEvent.Submit)
					uri = Uri.EMPTY
				}, text = stringResource(id = R.string.save)
			)
			Spacer(modifier = Modifier.height(15.dp))
		}
        item {
            Column(
                modifier = Modifier
	                .fillMaxWidth()
	                .height(400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PreviewContentFile(type.equals("img", true), file)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
	}
}

@Composable
private fun CalendarPick(onEvent: (AmountFormEvent) -> Unit, state: AmountFormState) {
	var pickedDate by remember { mutableStateOf(LocalDate.now()) }
	val formattedDate by remember { derivedStateOf { localDate(date = pickedDate) } }
	val dateDialogState = rememberMaterialDialogState()

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceAround,
		verticalAlignment = Alignment.CenterVertically
	) {
		CustomOutlineTextField(
			modifier = Modifier.fillMaxWidth(0.5f),
			enabled = true, text = formattedDate,
			onValueChange = {},
			error = state.amountDateError != null,
			errorMessage = state.amountDateError
		)

		Spacer(modifier = Modifier.width(20.dp))

		CustomButton(
			modifier = Modifier.fillMaxSize(),
			onClick = { dateDialogState.show() },
			enable = true,
			text = "amount date"
		)
	}
	MaterialDialog(
		dialogState = dateDialogState,
		buttons = {
			positiveButton(text = "OK") { }
			negativeButton(text = "") { }
		}) {
		datepicker(
			initialDate = LocalDate.now(),
			title = "select a date",
			yearRange = LocalDate.now().year.rangeTo(LocalDate.now().year + 3),
		) {
			onEvent(AmountFormEvent.AmountDate(localDate(date = it)))
			pickedDate = it
		}
	}
}

@Composable
private fun EntranceButton(
	modifier: Modifier = Modifier,
	exchange: String,
	entrance: Boolean,
	mutableEntrance: MutableState<Boolean>,
	onEvent: (AmountFormEvent) -> Unit,
	state: AmountFormState,
) {
	Column(
		modifier = modifier.height(78.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		CustomText(
			text = state.entranceError ?: "",
			upperCase = true, color = Color400, textWeight = FontWeight.Bold,
			textDecoration = TextDecoration.Underline
		)
		Spacer(modifier = Modifier.height(15.dp))
		Row(
			modifier = modifier.fillMaxSize(),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			CustomCheckBox(checked = entrance, onCheckedChange = {
				mutableEntrance.value = !mutableEntrance.value
				onEvent(AmountFormEvent.EntranceEventChange(mutableEntrance.value))
			})
			CustomText(text = exchange, upperCase = true, textWeight = FontWeight.Bold)
		}
		Spacer(modifier = Modifier.height(30.dp))
	}
}


@Composable
private fun PreviewContentFile(image: Boolean, file: Uri?) {
	val context = LocalContext.current
	if (image) {
		AsyncImage(
			modifier = Modifier
				.fillMaxWidth(0.9f)
				.border(2.dp, color = Color500, RoundedCornerShape(5)),
			model = file, contentDescription = "",
		)
	} else {
		if (file == null) return
		var bytes = getByteArrayFromUri(context, file)
		bytes = decompressData(bytes)
		ComposePDFViewer(byteArray = bytes)
	}
}

@PreviewLightDark
@Composable
private fun Preview() {
	val resource = Channel<Resource<Any>>()
	SharedReceipt(Uri.EMPTY, "img", resource.receiveAsFlow(),AmountFormState()) {}
}