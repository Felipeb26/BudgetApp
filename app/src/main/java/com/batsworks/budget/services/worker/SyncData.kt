package com.batsworks.budget.services.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.Collection
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.UserEntity
import com.batsworks.budget.domain.entity.UserFirebaseEntity
import com.batsworks.budget.domain.entity.toDTO
import com.batsworks.budget.domain.repository.CustomRepository
import com.batsworks.budget.services.notification.Notifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

class SyncData(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

	private val TAG: String = "SYNC_DATA"
	private val userDaoRepository: UsersDao = BudgetApplication.database.getUsersDao()
	private val amountDaoRepository: AmountDao = BudgetApplication.database.getAmountDao()

	/** Remote Repository dependency
	 * UserDTO && AmountEntity
	 */
	private var userRepository =
		CustomRepository(Collection.USERS.path, UserFirebaseEntity::class.java)
	private var amountRepository =
		CustomRepository(Collection.AMOUNTS.path, AmountFirebaseEntity::class.java)

	override suspend fun doWork(): Result {
		val scope = CoroutineScope(Dispatchers.Main)
		val notification = Notifications(context)
		Log.d(TAG, "working")
		return try {
			val usersNotSync = userDaoRepository.findNotSyncData()
			val amountsNotSync = amountDaoRepository.findNotSync()

			if (usersNotSync.isNotEmpty() || amountsNotSync.isNotEmpty()) {
				notification.showLoadingNotification(context, "Syncing saved data")
			} else return Result.success()

			usersNotSync.forEach { syncUsers(it) }
			amountsNotSync.forEach {
				var amount = i
				amount.file?.let { file ->
					val taskSnapshot =
						amountRepository.saveFile(file, "${amount.chargeName}.${amount.extension}")
							.await()
					amount = amount.withRef(taskSnapshot.uploadSessionUri)
				}
				amountRepository.save(toDTO(amount)).await()
				amountDaoRepository.save(amount.withSync(true))
			}

			Log.d(TAG, "worked")
			Result.success()
		} catch (e: Exception) {
			Log.d(TAG, e.message ?: "an error happer")
			notification.showBasicNotification("A error has happen during the syncing")
			Result.failure()
		}
	}

	private fun syncUsers(user: UserEntity) {

	}


}