package com.batsworks.budget.services.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.R
import com.batsworks.budget.components.Resource
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.Collection
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.entity.UserFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.services.notification.Notifications
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.UUID

class SyncData(val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val resourceEventChannel = Channel<Resource<Any>>()
    private val resourceEventFlow = resourceEventChannel.receiveAsFlow()

    private val userDaoRepository: UsersDao = BudgetApplication.database.getUsersDao()
    private val amountDaoRepository: AmountDao = BudgetApplication.database.getAmountDao()
    private var userRepository =
        CustomRepository(Collection.USERS.path, UserFirebaseEntity::class.java)
    private var amountRepository =
        CustomRepository(Collection.AMOUNTS.path, AmountFirebaseEntity::class.java)


    private val dataSync: List<DataSyncFactory> = listOf(
        SyncUserData(),
        SyncAmountData(
            context,
            resourceEventChannel,
            userDaoRepository,
            amountDaoRepository,
            amountRepository
        )
    )

    companion object {
        const val TAG: String = "SYNC_DATA"
        var time = 0
    }

    /** Remote Repository dependency
     * UserDTO && AmountEntity
     */

    override suspend fun doWork(): Result {
        val notification = Notifications(context)
        return try {
            Log.d(TAG, "RODOU $time")

     llllllllll       resourceEventFlow.collect { event ->
                when (event) {
                    is Resource.Loading -> {
                        notification.showLoadingNotification(context)
                    }

                    is Resource.Failure -> {
                        z.showBasicNotification(event.error ?: context.getString(R.string.adding_bill_error))
                    }

                    is Resource.Sucess -> {
                        notification.showBasicNotification(context.getString(R.string.adding_bill_sucess))
                    }
                }
            }

            val toSync = dataSync.firstOrNull { it.needsUpdate() }
            if (toSync == null) {
                notification.showBasicNotification("nada para atualizar agora")
                return Result.success()
            } else toSync.update()


            val toBring = dataSync.firstOrNull { it.needsBringData() }
            if (toBring == null) {
                notification.showBasicNotification("nenhum dado para trazer")
                return Result.success()
            } else toSync.save()

//			if (syncId.isNotNull()) return Result.success()
//			syncId = params.id
//			val usersNotSync = userDaoRepository.findNotSyncData()
//			val amountsNotSync = amountDaoRepository.findNotSync()
//
//			usersNotSync.forEach { syncUsers(it) }
//
//
//			if (amountsNotSync.isNotEmpty()) {
//				notification.showLoadingNotification(context, "Syncing saved data")
//			} else return Result.success()
//
//			amountsNotSync.forEach {
//				var amount = it
//				amount.file?.let { file ->
//					val taskSnapshot =
//						amountRepository.saveFile(
//							file,
//							"${amount.chargeName}.${amount.extension}"
//						).await()
//					amount = amount.withRef(taskSnapshot.uploadSessionUri)
//				}
//				amountRepository.save(toDTO(amount)).await()
//				amountDaoRepository.save(amount.withSync(true))
//			}
//			Log.d(TAG, "worked")
//			syncId = null
//			notification.showBasicNotification("Synchronization has finalize")
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, e.message ?: "an error happer")
            notification.showBasicNotification("A error has happen during the syncing")
            Result.failure()
        }
    }

    private fun syncUsers(user: UserEntity) {
        Log.d(TAG, "Updating user: ${user.email}")
    }


}