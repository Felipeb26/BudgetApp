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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.buttons.CustomButton
import com.batsworks.budget.components.buttons.CustomCheckBox
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.localDate
import com.batsworks.budget.components.visual_transformation.CurrencyTransformation
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.add.AmountFormEvent
import com.batsworks.budget.ui.view_model.add.AmountFormState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun SharedReceipt(file: Uri, state: AmountFormState, onEvent: (AmountFormEvent) -> Unit) {
	Log.d("VALUE", file.toString())
	val exchanges = arrayOf("entrance", "output")
	val uri by remember { mutableStateOf(file) }
	val entrance = remember { mutableStateOf(false) }

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
				onValueChange = {}
			)
			CustomOutlineTextField(
				modifier = Modifier.fillMaxWidth(0.95f),
				transformation = CurrencyTransformation(),
				labelText = stringResource(id = R.string.bill_value),
				onValueChange = {}
			)
		}
		item { CalendarPick(onEvent, state) }
		item {
			Row {
				exchanges.forEach { exchange ->
					EntranceButton(
						modifier = Modifier.weight(1f),
						exchange = exchange,
						entrance.value, entrance,
						onEvent, state
					)
				}
			}
		}
		item {
			AsyncImage(
				modifier = Modifier
					.fillMaxWidth()
					.height(500.dp)
					.border(1.dp, color = Color300),
				model = uri,
				contentDescription = ""
			)
		}
//		CalendarPick(onEvent, state)
//		Row(
//			modifier = Modifier.fillMaxWidth(),
//			horizontalArrangement = Arrangement.SpaceEvenly
//		) {
//			exchangeTypes.forEachIndexed { index, exchange ->
//				if ((index % 2) != 0) {
//					EntranceButton(
//						Modifier.weight(1f),
//						exchange,
//						!entrance.value,
//						entrance,
//						onEvent,
//						state
//					)
//				} else {
//					EntranceButton(
//						Modifier.weight(1f),
//						exchange,
//						entrance.value,
//						entrance,
//						onEvent,
//						state
//					)
//				}
//			}
//		}
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
			isUpperCase = true, color = Color400, textWeight = FontWeight.Bold,
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
			CustomText(text = exchange, isUpperCase = true, textWeight = FontWeight.Bold)
		}
		Spacer(modifier = Modifier.height(30.dp))
	}
}


@PreviewLightDark
@Composable
private fun Preview() {
	SharedReceipt(Uri.EMPTY, AmountFormState()) {}
}