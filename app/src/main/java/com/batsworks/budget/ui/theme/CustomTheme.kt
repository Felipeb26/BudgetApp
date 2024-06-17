package com.batsworks.budget.ui.theme

import androidx.compose.ui.graphics.Color

class CustomTheme(val theme: Theme = Theme.NATURE) {

	init {
		when (theme) {
			Theme.COLD -> coldTheme()
			Theme.NATURE -> natureTheme()
			Theme.THANOS -> thanosTheme()
			Theme.BLOOD -> thanosTheme()
		}
	}

	private fun thanosTheme() {
		Color50 = Color(0xFFf3f2fb)
		Color100 = Color(0xFFeae8f7)
		Color200 = Color(0xFFd8d5f0)
		Color300 = Color(0xFFc0bae7)
		Color400 = Color(0xFFaa9edb)
		Color500 = Color(0xFF9a86ce)
		Color600 = Color(0xFF8b6dbe)
		Color700 = Color(0xFF785ca6)
		Color800 = Color(0xFF5c487f)
		Color900 = Color(0xFF2f273f)
		Color950 = Color(0xFF51426d)
	}

	private fun coldTheme() {
		Color50 = Color(0xFFF2F7FB)
		Color100 = Color(0xFFe7f0f8)
		Color200 = Color(0xFFd3e2f2)
		Color300 = Color(0xFFb9cfe8)
		Color400 = Color(0xFF9cb6dd)
		Color500 = Color(0xFF839dd1)
		Color600 = Color(0xFF6a7fc1)
		Color700 = Color(0xFF6374ae)
		Color800 = Color(0xFF4a5989)
		Color900 = Color(0xFF414e6e)
		Color950 = Color(0xFF262c40)
	}

	private fun natureTheme(){
		Color50 = Color(0xFFf3faf6)
		Color100 = Color(0xFFd8efe4)
		Color200 = Color(0xFFb1dec9)
		Color300 = Color(0xFF64af92)
		Color400 = Color(0xFF64af92)
		Color500 = Color(0xFF3e8e70)
		Color600 = Color(0xFF3e8e70)
		Color700 = Color(0xFF2a5b4b)
		Color800 = Color(0xFF254a3e)
		Color900 = Color(0xFF223f35)
		Color950 = Color(0xFF0f241e)
	}
}

sealed interface Theme {
	data object THANOS : Theme
	data object NATURE : Theme
	data object BLOOD : Theme
	data object COLD : Theme
}