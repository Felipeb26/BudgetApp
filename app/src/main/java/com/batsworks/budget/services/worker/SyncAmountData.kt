package com.batsworks.budget.services.worker

import android.util.Log
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.toDTO
import com.batsworks.budget.domain.repository.CustomRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class SyncAmountData(
	private val userDao: UsersDao,
	private val amountDao: AmountDao,
	private val amountRepository: CustomRepository<AmountFirebaseEntity>,
) : DataSyncFactory {

	override suspend fun save() {
		val user = userDao.findUser()
		val amountsSaved = amountRepository.findByParam("user", user.firebaseId).await()
		amountsSaved.forEach { documents ->
			val amount = documents.toObject(AmountFirebaseEntity::class.java).toEntity()
			amountDao.save(amount)
		}
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
			val amount = document.toObject(AmountFirebaseEntity::class.java)
			amount?.let {
				val entity = it.toEntity()
				val file = amountRepository.retrieveFile(entity.chargeName.plus(".").plus(entity.extension))
				amountDao.save(entity.withUser(user.firebaseId).withFile(file))
			}
		}
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