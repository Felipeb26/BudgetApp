package com.batsworks.budget.navigation

import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.batsworks.budget.ui.view_model.add.AddViewModel
import com.batsworks.budget.ui.views.SharedReceipt

fun NavGraphBuilder.resourcesViewNavigation(navController: NavController) {
	composable<Screen.SharedReceiptScreen> {
		val args = it.toRoute<Screen.SharedReceiptScreen>()

		val model = hiltViewModel<AddViewModel>()
		val uri = Uri.parse(args.fileUri)

		SharedReceipt(
			uri, args.fileType,
			model.resourceEventFlow,
			model.state,
			model::onEvent
		)
	}
}
