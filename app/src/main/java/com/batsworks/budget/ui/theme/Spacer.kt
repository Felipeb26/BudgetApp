package com.batsworks.budget.ui.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpaceWithDivider(size: Int = 40, dividerThickness: Int = 2) {
    Spacer(modifier = Modifier.height((size / 2).dp))
    HorizontalDivider(color = textColor, thickness = dividerThickness.dp)
    Spacer(modifier = Modifier.height((size / 2).dp))
}
@Composable
fun DeafultSpacer(size: Int = 40) {
    Spacer(modifier = Modifier.height(size.dp))
}