package com.batsworks.budget.services.worker

class SyncUserData: DataSyncFactory {
	override suspend fun save() {
		TODO("Not yet implemented")
	}

	override suspend fun update() {
		TODO("Not yet implemented")
	}

	override suspend fun needsUpdate(): Boolean {
		return false
	}

	override suspend fun needsBringData(): Boolean {
		return false
	}
}