package com.batsworks.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.batsworks.budget.components.fields.CustomTextField
import com.batsworks.budget.ui.theme.Color600


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownMenu(
	modifier: Modifier = Modifier,
	itens: List<T> = emptyList(),
	onExpandChage: (Boolean) -> Unit,
	onDismiss: () -> Unit,
	expanded: Boolean = false,
	onValueChange: (String) -> Unit,
	selectText: String = "",
	isUpper: Boolean = true,
	weight: FontWeight = FontWeight.Normal
) {
	Box(modifier = modifier) {
		ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandChage) {
			CustomTextField(
				modifier = Modifier.menuAnchor(), isUpper = true,
				value = selectText, onValueChange = onValueChange,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
			)

			ExposedDropdownMenu(
				modifier = Modifier.background(Color600),
				expanded = expanded,
				onDismissRequest = onDismiss
			) {
				itens.forEachIndexed { index, s ->
					if (s is Int) {
						val string = stringResource(id = s)
						DropdownMenuItem(
							contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
							text = { CustomText(text = string, isUpperCase = isUpper, capitalize = !isUpper, textWeight = weight) },
							onClick = {
								onValueChange.invoke(itens[index].toString())
								onExpandChage.invoke(false)
							}
						)
					}
					if (s is String) {
						DropdownMenuItem(
							contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
							text = { CustomText(text = s, isUpperCase = true) },
							onClick = {
								onValueChange.invoke(itens[index].toString())
								onExpandChage.invoke(false)
							}
						)
					}
				}
			}
		}
	}
}