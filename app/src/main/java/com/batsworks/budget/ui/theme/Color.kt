package com.batsworks.budget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

var Color50 = Color(0xFFf3f2fb)
var Color100 = Color(0xFFeae8f7)
var Color200 = Color(0xFFd8d5f0)
var Color300 = Color(0xFFc0bae7)
var Color400 = Color(0xFFaa9edb)
var Color500 = Color(0xFF9a86ce)
var Color600 = Color(0xFF8b6dbe)
var Color700 = Color(0xFF785ca6)
var Color800 = Color(0xFF5c487f)
var Color900 = Color(0xFF2f273f)
var Color950 = Color(0xFF51426d)

val customDarkBackground
	@Composable
	get() = if (isSystemInDarkTheme()) Color600.copy(0.4f) else Color.White.copy(0.6f)
val customBackground
	@Composable
	get() = if (isSystemInDarkTheme()) Color800.copy(alpha = 0.7f) else Color.White

val textColor
	@Composable
	get() = if (isSystemInDarkTheme()) Color50 else Color950
