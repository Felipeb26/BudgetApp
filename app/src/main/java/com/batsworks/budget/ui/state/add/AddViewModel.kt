package com.batsworks.budget.ui.state.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.domain.dao.AmountDao
import kotlinx.coroutines.launch

class AddViewModel(
	private val localRepository: AmountDao = BudgetApplication.database.getAmountDao()
) : ViewModel() {

	init {
		viewModelScope.launch {
			localRepository.findAll()
		}
	}

}