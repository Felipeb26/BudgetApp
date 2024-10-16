package com.batsworks.budget.ui.view_model.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.ui.theme.THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val localRepository: UsersDao) : ViewModel() {

    var user by mutableStateOf<UserEntity?>(null)
    init {
    	runBlocking {
            user = localRepository.findUser()
        }
    }

    fun saveTheme(theme: THEME) {
        viewModelScope.launch {
            localRepository.updateTheme(theme.theme)
        }
    }
}