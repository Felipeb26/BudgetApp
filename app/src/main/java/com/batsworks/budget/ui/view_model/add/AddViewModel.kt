package com.batsworks.budget.ui.view_model.add

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.components.AJUST_TAG
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.files.getFileType
import com.batsworks.budget.components.formatter
import com.batsworks.budget.components.localDate
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.ui.view_model.ValidationResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class AddViewModel(
    private val chargeNameValidation: ValidateChargeName = ValidateChargeName(),
    private val chargeValueValidation: ValidateChargeValue = ValidateChargeValue(),
    private val fileValidation: ValidateFileVoucher = ValidateFileVoucher(),
    private val entranceValidation: ValdateEntrance = ValdateEntrance(),
    private val amountDateValidate: ValidateAmountDate = ValidateAmountDate(),
    private val userLocalRepository: UsersDao = BudgetApplication.database.getUsersDao(),
    private val amountLocalRepository: AmountDao = BudgetApplication.database.getAmountDao(),
	private val repository: CustomRepository<AmountEntity>
) : ViewModel() {

    private val tag: String = AddViewModel::class.java.name
    private val now = LocalDate.now().format(formatter())
    var state by mutableStateOf(AmountFormState())

    private val resourceEventChannel = Channel<Resource<Any>>()
    val resourceEventFlow = resourceEventChannel.receiveAsFlow()

    fun onEvent(event: AmountFormEvent) {
        when (event) {
            is AmountFormEvent.EntranceEventChange -> {
                state = state.copy(entrance = event.entrance)
            }

            is AmountFormEvent.ValueEventChange -> {
                state = state.copy(value = formatEventCharge(event.value))
            }

            is AmountFormEvent.ChargeNameEventChange -> {
                state = state.copy(chargeName = event.chargeName)
            }

            is AmountFormEvent.FileVoucher -> {
                state = state.copy(file = event.file)
            }

            is AmountFormEvent.AmountDate -> {
                state = state.copy(amountDate = event.amountDate)
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
        val amountDateResult = amountDateValidate.execute(state.amountDate.ifEmpty { now })
        val fileResult = if (fileIntance != null) {
            fileValidation.execute(state.file!!)
        } else ValidationResult(true)

        val hasError = listOf(
            chargeNameResult,
            fileResult,
            entranceResult,
            amountDateResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                chargeNameError = chargeNameResult.errorMessage,
                valueError = chargeValueResult.errorMessage,
                entranceError = entranceResult.errorMessage,
                fileError = fileResult.errorMessage,
                amountDateError = amountDateResult.errorMessage
            )
            Log.d(AJUST_TAG(tag), "Foram encontrados erros")
            return@launch
        }
        resourceEventChannel.send(Resource.Loading(true))
        saveAmout(state)
    }

    private fun saveAmout(state: AmountFormState) = viewModelScope.launch {
        try {
            val userEntity = userLocalRepository.findUser()
            val amout = AmountEntity(
                chargeName = state.chargeName,
                value = state.value,
                entrance = state.entrance,
                file = state.file,
                user = userEntity.firebaseId,
                amountDate = localDate(state.amountDate),
                extension = getFileType(state.file),
                size = state.file?.size ?: 0
            )
			state.file?.let { saveOnStorage(amout,it) }
            amountLocalRepository.save(amout)
            resourceEventChannel.send(Resource.Sucess(amout))
            resourceEventChannel.send(Resource.Loading(false))
        } catch (e: Exception) {
            resourceEventChannel.send(Resource.Failure(e.message ?: "A error has happen"))
            resourceEventChannel.send(Resource.Loading(false))
            return@launch
        }
    }

    private fun saveOnStorage(amout: AmountEntity, file: ByteArray){
        repository.saveFile(file, "${amout.chargeName}.${amout.extension}").addOnSuccessListener { snapshot ->
			Log.d(AJUST_TAG(tag), "ARQUIVO salvo com sucesso")
            amout.withRef(snapshot.uploadSessionUri)
        }.addOnFailureListener {
			Log.e(AJUST_TAG(tag), it.message ?: "an error has happen")
            throw Exception(it.message ?: it.cause?.message)
        }
    }


    private fun formatEventCharge(value: String): BigDecimal {
        if (value.length <= 1 && value.contains("."))
            return BigDecimal.ZERO
        val valueToUse = value.replace("R$", "").trim()
        return BigDecimal(valueToUse)
    }

}