package com.batsworks.budget.ui.view_model.profile

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.repository.CustomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
	private val repository: CustomRepository<UserEntity>? = null,
	private val localRepository: UsersDao = BudgetApplication.database.getUsersDao(),
) : ViewModel() {

	private val userEntityStateFlow = MutableStateFlow<UserEntity?>(null)
	val userEntity = userEntityStateFlow.asStateFlow()

	init {
		viewModelScope.launch {
			userEntityStateFlow.emit(localRepository.findUser())
		}
	}

	fun dontLoginWhenStart() {
		viewModelScope.launch {
			var user: UserEntity = localRepository.findUser()
			user = user.withLoginWhenEnter(false)
			localRepository.save(user)
		}
	}

}