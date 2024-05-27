package com.batsworks.budget.services.download

sealed interface Download {
    fun downloadFileUrl(url: String, imageName: String): Long
    fun downloadFileUrl(url: String): Long
    suspend fun downloadFromBytes(bytes: ByteArray)
}