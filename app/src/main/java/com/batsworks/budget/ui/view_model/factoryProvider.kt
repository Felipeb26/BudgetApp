package com.batsworks.budget.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun factoryProvider(viewmodel: ViewModel): ViewModelProvider.Factory {
	return object : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			return viewmodel as T
		}
	}
}