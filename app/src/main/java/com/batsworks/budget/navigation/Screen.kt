package com.batsworks.budget.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.batsworks.budget.R
import kotlinx.serialization.Serializable

sealed interface Screen {
	@Serializable
	data object LoginScreen : Screen

	@Serializable
	data object SignUpScreen : Screen

	@Serializable
	data object MainScreen : Screen

	@Serializable
	data object SettingScreen : Screen

	@Serializable
	data object HomeScreen : Screen

	@Serializable
	data object HistoryScreen : Screen
	@Serializable
	data object AdicionarScreen : Screen
	@Serializable
	data object ProfileScreen : Screen
	@Serializable
	data object PlusScreen : Screen
	@Serializable
	data object AccountsScreen : Screen
	@Serializable
	data class ReceiptScreen(var idReceipt: Int) : Screen

	@Serializable
	data class SharedReceiptScreen(
		var fileUri: String,
		val fileType: FileType,
	) : Screen

	@Serializable
	data object GroupScreen : Screen
}

@Composable
fun iconForScreen(screen: Screen): ImageVector {
	return when (screen) {
		Screen.LoginScreen -> Icons.Filled.Lock
		Screen.SignUpScreen -> Icons.Filled.Person
		Screen.MainScreen -> Icons.Filled.Home
		Screen.HomeScreen -> Icons.Filled.Home
		Screen.AdicionarScreen -> Icons.Filled.Add
		Screen.HistoryScreen -> Icons.AutoMirrored.Filled.List
		Screen.ProfileScreen -> Icons.Filled.AccountCircle
		is Screen.ReceiptScreen -> Icons.Filled.ShoppingCart
		Screen.SettingScreen -> Icons.Filled.Settings
		is Screen.SharedReceiptScreen -> Icons.Filled.ShoppingCart
		Screen.PlusScreen -> ImageVector.vectorResource(R.drawable.ic_more_horiz)
		Screen.AccountsScreen -> ImageVector.vectorResource(R.drawable.ic_account_balance)
		Screen.GroupScreen -> ImageVector.vectorResource(R.drawable.ic_people)
	}
}

enum class FileType {
	PDF, IMG
}