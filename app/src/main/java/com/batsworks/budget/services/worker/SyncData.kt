package com.batsworks.budget.services.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.R
import com.batsworks.budget.components.Resource
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.UserFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.services.notification.Notifications
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SyncData(val context: Context, params: WorkerParameters) :
	CoroutineWorker(context, params) {

	private val resourceEventChannel = Channel<Resource<Any>>()
	private val resourceEventFlow = resourceEventChannel.receiveAsFlow()

	companion object {
		lateinit var amountRepository: CustomRepository<AmountFirebaseEntity>
		lateinit var userRepository: CustomRepository<UserFirebaseEntity>
		lateinit var userDaoRepository: UsersDao
		lateinit var amountDaoRepository: AmountDao

		lateinit var dataSync: List<DataSyncFactory>
		const val TAG: String = "SYNC_DATA"
		var time = 0
	}

	init {
		// remote database - firebase
		amountRepository = BudgetApplication.appModule.amountRepository
		userRepository = BudgetApplication.appModule.userRepository
		//local database - room
		userDaoRepository = BudgetApplication.database.getUsersDao()
		amountDaoRepository = BudgetApplication.database.getAmountDao()

		dataSync = listOf(SyncUserData(), SyncAmountData(
			context,
			resourceEventChannel,
			userDaoRepository,
			amountDaoRepository,
			amountRepository
		))
	}

	/** Remote Repository dependency
	 * UserDTO && AmountEntity
	 */

	override suspend fun doWork(): Result {
		val notification = Notifications(context)
		return try {
			Log.d(TAG, "RODOU $time")
			resourceEventFlow.collect { event ->
				when (event) {
					is Resource.Loading -> {
						notification.showLoadingNotification(context)
					}

					is Resource.Failure -> {
						notification.showBasicNotification(
							event.error ?: context.getString(R.string.adding_bill_error)
						)
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

			Result.success()
		} catch (e: Exception) {
			Log.d(TAG, e.message ?: "an error happer")
			notification.showBasicNotification("A error has happen during the syncing")
			Result.failure()
		}
	}

}