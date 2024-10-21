package com.batsworks.budget.ui.components.buttons

import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.batsworks.budget.ui.theme.Color100
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950

@Composable
fun swicthColors():SwitchColors{
    return SwitchDefaults.colors(
        checkedThumbColor = Color.Transparent,
        checkedTrackColor = Color500.copy(0.4f),
        checkedBorderColor = Color400,
        checkedIconColor = Color400,

        uncheckedThumbColor = Color800.copy(0.5f),
        uncheckedTrackColor = Color500.copy(0.4f),
        uncheckedBorderColor = Color400,
        uncheckedIconColor = Color950.copy(0.8f),

        disabledCheckedThumbColor = Color800.copy(0.4f),
        disabledCheckedTrackColor = Color700,
        disabledUncheckedIconColor = Color100.copy(0.4f),
        disabledUncheckedThumbColor = Color800.copy(0.4f),
        disabledUncheckedTrackColor = Color700
    )
}