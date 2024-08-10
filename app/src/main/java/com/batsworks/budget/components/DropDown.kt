package com.batsworks.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.batsworks.budget.components.fields.CustomTextField
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.customBackground


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
    weight: FontWeight = FontWeight.Bold
) {
    var text by remember { mutableStateOf(selectText) }

    Box(modifier = modifier) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandChage) {
            CustomTextField(
                modifier = Modifier.menuAnchor(), isUpper = isUpper,
                value = text, onValueChange = onValueChange,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.rotate(if (expanded) 180f else 0f),
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null, tint = customBackground
                    )
                }
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(Color400.copy(0.8f)),
                expanded = expanded,
                onDismissRequest = onDismiss
            ) {
                itens.forEachIndexed { index, s ->
                    if (s is Int) {
                        val string = stringResource(id = s)
                        DropdownMenuItem(
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            text = {
                                CustomText(
                                    text = string,
                                    isUpperCase = isUpper,
                                    capitalize = !isUpper,
                                    textWeight = weight,
                                    space = TextUnit(2f, TextUnitType.Sp)
                                )
                            },
                            onClick = {
                                onValueChange.invoke(itens[index].toString())
                                text = itens[index].toString()
                                onExpandChage.invoke(false)
                            }
                        )
                    }
                    if (s is String) {
                        DropdownMenuItem(
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            text = {
                                CustomText(
                                    text = s,
                                    isUpperCase = isUpper,
                                    capitalize = !isUpper,
                                    textWeight = weight,
                                    space = TextUnit(2f, TextUnitType.Sp)
                                )
                            },
                            onClick = {
                                onValueChange.invoke(itens[index].toString())
                                text = itens[index].toString()
                                onExpandChage.invoke(false)
                            }
                        )
                    }
                }
            }
        }
    }
}