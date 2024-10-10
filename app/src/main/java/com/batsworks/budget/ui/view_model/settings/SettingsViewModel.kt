package com.batsworks.budget.ui.view_model.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.ui.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val localRepository: UsersDao) : ViewModel() {

    fun saveTheme(theme: Theme) {
        viewModelScope.launch {
            localRepository.updateTheme(theme.theme)
        }
    }
}