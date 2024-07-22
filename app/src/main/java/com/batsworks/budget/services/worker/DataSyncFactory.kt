package com.batsworks.budget.services.worker

interface DataSyncFactory {
	suspend fun save()
	suspend fun bring()
	suspend fun needsUpdate(): Boolean

}