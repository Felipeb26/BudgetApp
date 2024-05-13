package com.batsworks.budget.ui.view_model.add

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.ajustTag
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.use_cases.ValidationResult
import com.batsworks.budget.domain.use_cases.amout.ValdateEntrance
import com.batsworks.budget.domain.use_cases.amout.ValidateChargeName
import com.batsworks.budget.domain.use_cases.amout.ValidateChargeValue
import com.batsworks.budget.domain.use_cases.amout.ValidateFileVoucher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddViewModel(
    private val chargeNameValidation: ValidateChargeName = ValidateChargeName(),
    private val chargeValueValidation: ValidateChargeValue = ValidateChargeValue(),
    private val fileValidation: ValidateFileVoucher = ValidateFileVoucher(),
    private val entranceValidation: ValdateEntrance = ValdateEntrance(),
    private val userRepository: UsersDao = BudgetApplication.database.getUsersDao(),
    private val localRepository: AmountDao = BudgetApplication.database.getAmountDao()
) : ViewModel() {

    private val TAG: String = AddViewModel::class.java.name
    var state by mutableStateOf(AmountFormState())

    private val resourceEventChannel = Channel<Resource<Any>>()
    val resourceEventFlow = resourceEventChannel.receiveAsFlow()

    fun onEvent(event: AmountFormEvent) {
        when (event) {
            is AmountFormEvent.EntranceEventChange -> {
                state = state.copy(entrance = event.entrance)
            }

            is AmountFormEvent.ValueEventChange -> {
                state = state.copy(value = event.value)
            }

            is AmountFormEvent.ChargeNameEventChange -> {
                state = state.copy(chargeName = event.chargeName)
            }

            is AmountFormEvent.FileVoucher -> {
                state = state.copy(file = event.file)
            }

            is AmountFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() = viewModelScope.launch {
        val fileIntance = state.file
        val chargeNameResult = chargeNameValidation.execute(state.chargeName)
        val chargeValueResult = chargeValueValidation.execute(state.value)
        val entranceResult = entranceValidation.execute(state.entrance)
        val fileResult = if (fileIntance != null) {
            fileValidation.execute(state.file!!)
        } else ValidationResult(true)

        val hasError = listOf(
            chargeNameResult,
            fileResult,
            entranceResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                chargeNameError = chargeNameResult.errorMessage,
                valueError = chargeValueResult.errorMessage,
                entranceError = entranceResult.errorMessage,
                fileError = fileResult.errorMessage,
            )
            Log.d(ajustTag(TAG), "Foram encontrados erros")
            return@launch
        }
        resourceEventChannel.send(Resource.Loading(true))
        saveAmout(state)
    }

    private fun saveAmout(state: AmountFormState) = viewModelScope.launch {
        try {
            val userEntity = userRepository.findUser()
            val amout = AmountEntity(
                chargeName = state.chargeName,
                value = state.value,
                entrance = state.entrance,
                file = state.file,
                user = userEntity.firebaseId
            )
            localRepository.save(amout)
            resourceEventChannel.send(Resource.Loading(false))
            resourceEventChannel.send(Resource.Sucess(amout))
        } catch (e: Exception) {
            resourceEventChannel.send(Resource.Loading(false))
            resourceEventChannel.send(Resource.Failure(e.message ?: "A error has happen"))
            return@launch
        }
    }


}