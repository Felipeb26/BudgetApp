package com.batsworks.budget.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun factoryProvider(className: String, vararg args: Any): ViewModelProvider.Factory {
	return object : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			return Class.forName(className).getConstructor().newInstance(args) as T
		}
	}
}