package com.batsworks.budget.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


fun paddingScreen(): PaddingValues {
    return PaddingValues(Padding.BIG, Padding.X_EXTREM, Padding.BIG, Padding.BIG)
}

fun paddingScreen(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): PaddingValues {
    return PaddingValues(horizontal, vertical)
}

object Padding {
    val NONE = 0.dp
    val X_SMALL = 4.dp
    val SMALL = 5.dp
    val MEDIUM = 10.dp
    val X_MEDIUM = 15.dp
    val BIG = 20.dp
    val X_BIG = 25.dp
    val LARGE = 30.dp
    val X_LARGE = 35.dp
    val XX_LARGE = 40.dp
    val EXTREM = 45.dp
    val X_EXTREM = 50.dp
}