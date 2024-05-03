package com.batsworks.budget.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

fun paddingScreen(): PaddingValues {
	return PaddingValues(20.dp, 50.dp, 20.dp, 20.dp)
}

fun paddingScreen(horizontal: Int, vertical: Int): PaddingValues {
	return PaddingValues(horizontal.dp, vertical.dp)
}

