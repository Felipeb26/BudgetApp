package com.batsworks.budget.services.worker

import com.batsworks.budget.data.dao.AmountDAO
import com.batsworks.budget.data.dao.DeletedAmountDao
import com.batsworks.budget.data.entity.AmountFirebaseEntity
import com.batsworks.budget.data.repository.CustomRepository

class SyncDeletedAmountData(
	private val amountDao: AmountDAO,
	private val deletedAmountDao: DeletedAmountDao,
	private val amountRepository: CustomRepository<AmountFirebaseEntity>,
) : DataSyncFactory {
	override suspend fun save() {
		TODO("Not yet implemented")
	}

	override suspend fun update() {
		val deletedAmounts = deletedAmountDao.findAll()
		deletedAmounts.forEach { deleted ->
			deleted.firebaseId?.let { id ->
				val amount = amountDao.findById(deleted.localId)
				amount.fileRef?.let { ref -> ref.path?.let { amountRepository.deleteFile(it) } }
				amountRepository.delete(id).isSuccessful
				deletedAmountDao.deleteByFirebaseId(deleted.firebaseId)
			}
			deletedAmountDao.deleteById(deleted.id)
		}
	}

	override suspend fun needsUpdate(): Boolean = deletedAmountDao.countAll() > 0


	override suspend fun needsBringData(): Boolean {
		return false
	}
}