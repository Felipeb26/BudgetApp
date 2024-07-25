package com.batsworks.budget.services.worker

import android.content.Context
import com.batsworks.budget.components.Resource
import com.batsworks.budget.domain.dao.AmountDao
import com.batsworks.budget.domain.dao.UsersDao
import com.batsworks.budget.domain.entity.AmountFirebaseEntity
import com.batsworks.budget.domain.entity.toDTO
import com.batsworks.budget.domain.repository.CustomRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    override suspend fun needsBringData(): Boolean {
        val amount = amountDao.findLastAmount() ?: return false
        val remoteAmounts = amount.firebaseId?.let { amountRepository.findAfterId(it) } ?: return false
        val amounts = remoteAmounts.await()
        return amounts.size() > 0
    }
}