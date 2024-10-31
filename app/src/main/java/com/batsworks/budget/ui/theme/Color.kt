package com.batsworks.budget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

var Color50 = Color(0xFFfff0f0)
var Color100 = Color(0xFFffdddd)
var Color200 = Color(0xFFffc0c0)
var Color300 = Color(0xFFff9494)
var Color400 = Color(0xFFff5757)
var Color500 = Color(0xFF413B3B)
var Color600 = Color(0xFFB45F5F)
var Color700 = Color(0xFF413B3B)
var Color800 = Color(0xFFF16868)
var Color900 = Color(0xFF9E5B5B)
var Color950 = Color(0xFF500000)

var ColorLink = Color(0xFF0000EE)

var ColorCardEmprestimo = Color(0xFF800000)
var ColorCardCartoes = Color(0xFFFFD700)
var ColorCardInvestimentos = Color(0xFF228B22)

val Gray = Color(0xFF909090)

val customBackground
    @Composable
    get() = if (isSystemInDarkTheme()) Color500.copy(0.9f) else Color.White

val textColor
    @Composable
    get() = if (isSystemInDarkTheme()) Color50 else Color950
