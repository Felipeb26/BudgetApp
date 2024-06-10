package com.batsworks.budget.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.batsworks.budget.components.fields.CustomTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
	modifier: Modifier = Modifier,
	itens: List<String> = listOf("azul", "red", "fvnm"),
	onExpandChage: (Boolean) -> Unit,
	onDismiss: () -> Unit,
	expanded: Boolean = false,
	onValueChange: (String) -> Unit,
	selectText: String = "",
) {

	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandChage) {
			CustomTextField(
				modifier = Modifier.menuAnchor(), isUpper = true,
				value = selectText, onValueChange = onValueChange,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
			)

			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = onDismiss
			) {
				itens.forEachIndexed { index, s ->
					DropdownMenuItem(
						contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
						text = { CustomText(text = s) },
						onClick = {
							onValueChange.invoke(itens[index])
							onExpandChage.invoke(false)
						}
					)
				}
			}
		}
	}
}