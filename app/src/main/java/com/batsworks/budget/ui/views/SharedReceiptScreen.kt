package com.batsworks.budget.ui.views

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.batsworks.budget.R
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.visual_transformation.CurrencyTransformation
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.customBackground

@Composable
fun SharedReceipt(file: String) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.padding(0.dp)
			.padding(top = 30.dp, start = 15.dp, end = 15.dp),
		horizontalAlignment = Alignment.Start
	) {
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

		AsyncImage(
			modifier = Modifier
				.fillMaxWidth()
				.height(500.dp)
				.border(1.dp, color = Color300),
			model = file.let {
				Log.d("VALUE", it)
				Uri.parse(it)
			},
			contentDescription = ""
		)

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


@PreviewLightDark
@Composable
private fun Preview() {
	SharedReceipt("")
}