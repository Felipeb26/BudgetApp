package com.batsworks.budget.services.worker

import android.content.Context
import android.util.Log
import com.batsworks.budget.components.Resource
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.toDTO
import com.batsworks.budget.domain.repository.CustomRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.tasks.await

class SyncAmountData(
	private val context: Context,
	private val resourceEventChannel: Channel<Resource<Any>>,
	private val userDao: UsersDao,
	private val amountDao: AmountDao,
	private val amountRepository: CustomRepository<AmountFirebaseEntity>,
) : DataSyncFactory {

	override suspend fun save() {
		val user = userDao.findUser()
		val amountsSaved = amountRepository.findByParam("user", user.firebaseId).await()
		amountsSaved.forEach { documents ->
			resourceEventChannel.send(Resource.Loading(false))
			val amount = documents.toObject(AmountFirebaseEntity::class.java).toEntity(context)
			amountDao.save(amount)
		}
		resourceEventChannel.send(Resource.Sucess(""))
	}

	override suspend fun update() {
		val amountsNotSync = amountDao.findNotSync()
		amountsNotSync.forEach {
			var ae = it
			ae.file?.let { file ->
				val taskSnapshot =
					amountRepository.saveFile(file, "${ae.chargeName}.${ae.extension}").await()
				ae = ae.withRef(taskSnapshot.uploadSessionUri)
			}
			val amountReference = amountRepository.save(toDTO(ae)).await()
			amountDao.save(ae.withSync(true).withFirebaseId(amountReference.id))
		}
	}

	override suspend fun needsUpdate(): Boolean {
		return amountDao.findNotSync().isNotEmpty()
	}

	suspend fun save(amounts: List<DocumentSnapshot>) {
		val user = userDao.findUser()
		amounts.forEach { document ->
//			resourceEventChannel.(Resource.Loading(false))
			val amount = document.toObject(AmountFirebaseEntity::class.java)
			amount?.let {
				val entity = it.toEntity(context)
				amountDao.save(entity.withUser(user.firebaseId))
			}
		}
//		resourceEventChannel.send(Resource.Sucess(""))
	}

	override suspend fun needsBringData(): Boolean {
		val amountToFilter = amountDao.findAllAmountSync()
		val amounts = amountRepository.idNotIn(amountToFilter).await()

		Log.d("HOW", "${amounts.size}")
		if (amounts.isEmpty()) return false
		save(amounts)
		return false
	}
}