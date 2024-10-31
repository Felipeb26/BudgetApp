package com.batsworks.budget.ui.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.batsworks.budget.R
import com.batsworks.budget.components.animations.Loading
import com.batsworks.budget.data.entity.UserEntity
import com.batsworks.budget.language.LanguageSettings
import com.batsworks.budget.ui.components.buttons.swicthColors
import com.batsworks.budget.ui.components.menu.DropDownMenu
import com.batsworks.budget.ui.components.texts.CustomText
import com.batsworks.budget.ui.theme.Padding
import com.batsworks.budget.ui.theme.SpaceWithDivider
import com.batsworks.budget.ui.theme.custom.CustomTheme
import com.batsworks.budget.ui.theme.custom.THEME
import com.batsworks.budget.ui.theme.custom.findTheme
import com.batsworks.budget.ui.theme.custom.themes
import com.batsworks.budget.ui.theme.customBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration


@Composable
fun Setting(user: UserEntity?, coroutine: CoroutineScope, saveTheme: (THEME) -> Unit, forceDataSync: () -> Unit) {
    val (loading, setLoading) = remember { mutableStateOf(false) }

    val configurationItens: List<@Composable () -> Unit> = listOf(
        { LanguageContent(setLoading, coroutine) },
        { ThemeContent(user, saveTheme, setLoading, coroutine) },
        { UpdateTime() },
        { ForceToBringData(forceDataSync) }
    )

    if (loading) Loading()
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(customBackground)
        ) {
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
    }
}


@Composable
private fun LanguageContent(
    reloadScreen: (Boolean) -> Unit,
    coroutineScope: CoroutineScope,
) {
    var expanded by remember { mutableStateOf(false) }
    val languageSettings = LanguageSettings(LocalContext.current.resources.configuration)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.BIG, Padding.X_LARGE)
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
            itens = languageSettings.enabled.keys.toList(),
            selectText = languageSettings.current(),
            onValueChange = {
                languageSettings.selectAppLanguage(it.toInt())
                resetScreen(reloadScreen, coroutineScope)
            },
        )
    }
}


@Composable
private fun ThemeContent(
    user: UserEntity?,
    saveTheme: (THEME) -> Unit,
    reloadScreen: (Boolean) -> Unit,
    coroutineScope: CoroutineScope,
) {
    var expandedTheme by remember { mutableStateOf(false) }
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.MEDIUM, Padding.X_MEDIUM)
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
            selectText = user?.theme ?: "",
            onValueChange = { tema ->
                CustomTheme(view, findTheme(tema))
                saveTheme(findTheme(tema))
                resetScreen(reloadScreen, coroutineScope)
            }
        )
    }
}

@Composable
private fun UpdateTime() {
    var expanded by remember { mutableStateOf(false) }
    val updateTime = listOf("3h", "6h", "12h", "24h")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.BIG, Padding.X_LARGE)
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
private fun ForceToBringData(forceSync: () -> Unit) {
    var switchState by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(checked = switchState,
            onCheckedChange = {
                switchState = !switchState
                forceSync.invoke()
            }, colors = swicthColors(),
            thumbContent = {
                Icon(
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                    imageVector = Icons.Filled.Check,
                    contentDescription = "check button"
                )
            })
        CustomText(
            textStyle = MaterialTheme.typography.titleMedium,
            text = "Efetuar sincronização remota",
            textWeight = FontWeight.Bold
        )
    }
}


private fun resetScreen(reloadScreen: (Boolean) -> Unit, coroutineScope: CoroutineScope) {
    reloadScreen.invoke(true)
    coroutineScope.launch {
        delay(Duration.ofMillis(350))
        reloadScreen.invoke(false)
    }
}

@Composable
@PreviewLightDark
fun SettingWhite() {
    val coroutine = rememberCoroutineScope()
    Setting(UserEntity(theme = THEME.CHERRY.theme), coroutine,{}) {}
}