package com.batsworks.budget.ui.views

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.DropDownMenu
import com.batsworks.budget.ui.theme.customDarkBackground
import java.util.Locale

@Composable
fun Setting(navController: NavController) {
	var expanded by remember { mutableStateOf(true) }
	val locales: Map<String, Locale> = mapOf(
		"ingles" to Locale.ENGLISH,
		"pt-br" to Locale("pt-BR")
	)
//https://www.youtube.com/watch?v=ObgmK3BywKI
//	AppCompatDelegate.setApplicationLocales(
//		LocaleListCompat.forLanguageTags(locales.values[0])
//	)
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customDarkBackground),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		DropDownMenu(
			modifier = Modifier.weight(1f),
			onExpandChage = { expanded = !expanded },
			onDismiss = { expanded = !expanded },
			expanded = expanded,
			itens = locales.keys.toList(),
			onValueChange = {}
		)
	}
}

@Composable
@PreviewLightDark
fun SettingWhite() {
	Setting(navController = rememberNavController())
}