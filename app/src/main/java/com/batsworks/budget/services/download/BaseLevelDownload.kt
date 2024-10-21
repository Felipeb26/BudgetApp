package com.batsworks.budget.services.download

import kotlinx.coroutines.CancellableContinuation
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun downloadFile(path: String, bytes: ByteArray, continuation: CancellableContinuation<String>) {
    val file = FileOutputStream(path)
    try {
        file.write(bytes)
        file.flush()
        file.close()
        continuation.resume(path)
    } catch (e: Exception) {
        file.close()
        continuation.resumeWithException(e)
    }
}
