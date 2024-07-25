package com.batsworks.budget.services.worker

interface DataSyncFactory {
	suspend fun save()
	suspend fun update()
	suspend fun needsUpdate(): Boolean
	suspend fun needsBringData():Boolean

}