package com.batsworks.budget.ui.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.texts.CustomText
import com.batsworks.budget.components.DropDownMenu
import com.batsworks.budget.language.LanguageSettings
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.theme.CustomTheme
import com.batsworks.budget.ui.theme.SpaceWithDivider
import com.batsworks.budget.ui.theme.Theme
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.findTheme
import com.batsworks.budget.ui.theme.themes
import com.batsworks.budget.R


@Composable
fun Setting(navController: NavController, saveTheme: (Theme) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackground)
    ) {
        Content(navController, saveTheme)
    }

}

@Composable
private fun Content(navController: NavController, saveTheme: (Theme) -> Unit) {
    val configurationItens: List<@Composable () -> Unit> = listOf(
        { LanguageContent() },
        { ThemeContent(navController, saveTheme) },
        { UpdateTime() }
    )
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        configurationItens.forEachIndexed { index, contentFunction ->
            contentFunction()
            if (index < configurationItens.size - 1) {
                SpaceWithDivider()
            }
        }
    }
}


@Composable
private fun LanguageContent() {
    var expanded by remember { mutableStateOf(false) }
    val language = LanguageSettings()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 35.dp)
    ) {
        CustomText(
            text = stringResource(id = R.string.language), textWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline, capitalize = true,
            textStyle = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropDownMenu(
            modifier = Modifier.fillMaxWidth(),
            onExpandChage = { expanded = !expanded },
            onDismiss = { expanded = !expanded },
            expanded = expanded, isUpper = false,
            itens = language.enabled.keys.toList(),
            onValueChange = { language.selectAppLanguage(it.toInt()) }
        )
    }
}


@Composable
private fun ThemeContent(navController: NavController, saveTheme: (Theme) -> Unit) {
    var expandedTheme by remember { mutableStateOf(false) }
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        CustomText(
            text = stringResource(id = R.string.theme), textWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline, capitalize = true,
            textStyle = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropDownMenu(
            modifier = Modifier.fillMaxWidth(),
            onExpandChage = { expandedTheme = !expandedTheme },
            onDismiss = { expandedTheme = !expandedTheme },
            expanded = expandedTheme, isUpper = false,
            itens = themes, weight = FontWeight.Bold,
            onValueChange = { tema ->
                CustomTheme(view, findTheme(tema))
                saveTheme(findTheme(tema))
                easyNavigate(navController, Screen.SettingScreen.route)
            }
        )
    }
}

@Composable
fun UpdateTime() {
    var expanded by remember { mutableStateOf(false) }
    val updateTime = listOf("3h", "6h", "12h", "24h")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 35.dp)
    ) {
        CustomText(
            text = "Hora para atualizar", textWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline, capitalize = true,
            textStyle = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropDownMenu(
            modifier = Modifier.fillMaxWidth(),
            onExpandChage = { expanded = !expanded },
            onDismiss = { expanded = !expanded },
            expanded = expanded, isUpper = false,
            itens = updateTime,
            onValueChange = { Log.d("SELECIONADO", it) }
        )
    }
}


@Composable
@PreviewLightDark
fun SettingWhite() {
    Setting(navController = rememberNavController()) {}
}