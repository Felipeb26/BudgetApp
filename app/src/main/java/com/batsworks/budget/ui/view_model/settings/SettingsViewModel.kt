package com.batsworks.budget.ui.view_model.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.ui.theme.Theme
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val localRepository: UsersDao = BudgetApplication.database.getUsersDao(),
) : ViewModel() {


    fun saveTheme(theme: Theme) {
        viewModelScope.launch {
            localRepository.updateTheme(theme.theme)
        }
    }
}