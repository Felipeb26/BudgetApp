package com.batsworks.budget.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <VM: ViewModel> factoryProvider(initializer: () -> VM): ViewModelProvider.Factory {
	return object : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			return initializer() as T
		}
	}
}