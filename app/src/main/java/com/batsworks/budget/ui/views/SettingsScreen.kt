package com.batsworks.budget.ui.views

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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.DropDownMenu
import com.batsworks.budget.language.LanguageSettings
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.CustomTheme
import com.batsworks.budget.ui.theme.Theme
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.findTheme
import com.batsworks.budget.ui.theme.themes

@Composable
fun Setting(navController: NavController, saveTheme: (Theme) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val language = LanguageSettings()

    var expandedTheme by remember { mutableStateOf(false) }
    val view = LocalView.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DropDownMenu(
            modifier = Modifier.weight(1f),
            onExpandChage = { expanded = !expanded },
            onDismiss = { expanded = !expanded },
            expanded = expanded,
            itens = language.enabled.keys.toList(),
            onValueChange = { language.selectAppLanguage(it.toInt()) }
        )
        DropDownMenu(
            modifier = Modifier.weight(1f),
            onExpandChage = { expandedTheme = !expandedTheme },
            onDismiss = { expandedTheme = !expandedTheme },
            expanded = expandedTheme,
            itens = themes,
            onValueChange = { tema ->
                CustomTheme(view, findTheme(tema))
                saveTheme(findTheme(tema))
                easyNavigate(navController, Screen.SettingScreen.route)
            }
        )
    }
}

@Composable
@PreviewLightDark
fun SettingWhite() {
    Setting(navController = rememberNavController()){}
}