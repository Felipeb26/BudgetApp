package com.batsworks.budget.services.worker

import android.util.Log
import com.batsworks.budget.data.dao.AmountDAO
import com.batsworks.budget.data.dao.UsersDAO
import com.batsworks.budget.data.entity.AmountFirebaseEntity
import com.batsworks.budget.data.entity.toDTO
import com.batsworks.budget.data.repository.CustomRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await


class SyncAmountData(
    private val userDao: UsersDAO,
    private val amountDao: AmountDAO,
    private val amountRepository: CustomRepository<AmountFirebaseEntity>
) : DataSyncFactory {

    override suspend fun save() {
        val amountToFilter = amountDao.findAllAmountSync()
        val amounts = if (amountToFilter.isNotEmpty())
            amountRepository.idNotIn(amountToFilter).await()
        else amountRepository.findAllNotSync()
        save(amounts)
    }

    override suspend fun update() {
        val amountsNotSync = amountDao.findNotSync()

        amountsNotSync.forEach {
            var ae = it
            ae.file?.let { file ->
                ae = ae.withFileSize(it.size / 1048576)
                val taskSnapshot =
                    amountRepository.saveFile(file, "${ae.chargeName}.${ae.extension}").await()
                ae = ae.withRef(taskSnapshot.uploadSessionUri)
            }
            val amountReference = amountRepository.save(toDTO(ae.withSync(true))).await()
            amountDao.save(ae.withSync(true).withFirebaseId(amountReference.id))
        }
    }

    override suspend fun needsUpdate(): Boolean = amountDao.findNotSync().isNotEmpty()

    suspend fun save(amounts: List<DocumentSnapshot>) {
        val user = userDao.findUser()
        amounts.forEach { document ->
            val amount = document.toObject(AmountFirebaseEntity::class.java)
            amount?.let {
                val entity = it.toEntity().withSync(true)
                val file = amountRepository.retrieveFile(
                    entity.chargeName.plus(".").plus(entity.extension)
                )
                amountDao.save(entity.withUser(user.firebaseId).withFile(file))
            }
        }
    }

    override suspend fun needsBringData(): Boolean {
        val amountToFilter = amountDao.findAllAmountSync()
        val amounts = if (amountToFilter.isNotEmpty())
            amountRepository.idNotIn(amountToFilter).await()
        else amountRepository.findAllNotSync()

        Log.d("HOW", "${amounts.size}")
        return amounts.isNotEmpty()
    }
}