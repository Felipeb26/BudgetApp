package com.batsworks.budget.services.worker

import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.toDTO
import com.batsworks.budget.domain.repository.CustomRepository
import kotlinx.coroutines.tasks.await

class SyncAmountData(
	private val userDao: UsersDao,
	private val amountDao: AmountDao,
	private val amountRepository: CustomRepository<AmountFirebaseEntity>,
) : DataSyncFactory {

	override suspend fun save() {
		val user = userDao.findUser()
		amountRepository.findByParam("user", user.firebaseId).addOnSuccessListener {
		}
	}

	override suspend fun bring() {
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
}