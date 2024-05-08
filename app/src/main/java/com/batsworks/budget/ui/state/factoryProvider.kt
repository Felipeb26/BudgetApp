package com.batsworks.budget.ui.state

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun factoryProvider(viewmodel: ViewModel): ViewModelProvider.Factory {
	return object : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			return viewmodel as T
		}
	}
}