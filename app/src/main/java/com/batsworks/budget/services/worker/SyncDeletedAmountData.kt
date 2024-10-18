package com.batsworks.budget.services.worker

import com.batsworks.budget.domain.dao.DeletedAmountDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.repository.CustomRepository

class SyncDeletedAmountData(
	private val deletedAmountDao: DeletedAmountDao,
	private val amountRepository: CustomRepository<AmountFirebaseEntity>,
) : DataSyncFactory {
	override suspend fun save() {
		TODO("Not yet implemented")
	}

	override suspend fun update() {
		val deletedAmounts = deletedAmountDao.findAll()
		deletedAmounts.forEach {
			it.firebaseId?.let { id -> amountRepository.delete(id).result }
			deletedAmountDao.deleteById(it.id)
		}
	}

	override suspend fun needsUpdate(): Boolean = deletedAmountDao.countAll() > 0


	override suspend fun needsBringData(): Boolean {
		return false
	}
}