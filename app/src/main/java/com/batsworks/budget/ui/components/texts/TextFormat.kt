package com.batsworks.budget.ui.components.texts

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.batsworks.budget.R
import com.batsworks.budget.navigation.Screen


fun capitalizeStrings(text: String?): String {
    if (text.isNullOrEmpty()) return ""
    val values = text.split(" ").map { capitalize(it.trim()) }.toList()
    return values.joinToString(" ")
}

private fun capitalize(text: String?): String {
    if (text.isNullOrEmpty()) return ""
    val firstChar = text.substring(0, 1)
    val restOfChars = text.substring(1)
    return firstChar.toUpperCase(Locale.current).plus(restOfChars)
}

@Composable
fun formatScreenTitle(screen: Screen?): String {
    return when (screen) {
        Screen.AccountsScreen -> stringResource(id = R.string.account)
        Screen.AdicionarScreen -> stringResource(id = R.string.add)
        Screen.HistoryScreen-> stringResource(id = R.string.history)
        Screen.HomeScreen -> stringResource(id = R.string.home)
        Screen.PlusScreen -> stringResource(id = R.string.plus)
        Screen.ProfileScreen -> stringResource(id = R.string.profile)
        Screen.SettingScreen -> stringResource(id = R.string.settings)
        Screen.GroupScreen -> stringResource(id = R.string.enterprise_name)
        else -> stringResource(id = R.string.exit)
    }.toUpperCase(Locale.current)
}