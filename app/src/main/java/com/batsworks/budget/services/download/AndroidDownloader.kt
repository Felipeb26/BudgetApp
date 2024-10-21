package com.batsworks.budget.services.download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.batsworks.budget.utils.files.Folder
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URLConnection
import java.util.UUID


class AndroidDownloader(context: Context) : Download {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFileUrl(url: String, imageName: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(imageName.plus(".jpeg"))
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                imageName.plus(".jpeg")
            )
        return downloadManager.enqueue(request)
    }

    override fun downloadFileUrl(url: String): Long {
        val imageName = UUID.randomUUID().toString().plus(".jpeg")
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Downloading ".plus(imageName))
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, imageName)
        return downloadManager.enqueue(request)
    }

    override suspend fun downloadFromBytes(name: String, bytes: ByteArray): String =
        suspendCancellableCoroutine { continuation ->
            val `is`: InputStream = ByteArrayInputStream(bytes)
            val mimeType = URLConnection.guessContentTypeFromStream(`is`)
            val caminho = Folder.DOWNLOADS.path.plus("$name.${fileType(mimeType)}")
            downloadFile(caminho, bytes, continuation)
        }

    private fun fileType(s: String?): String {
        if (s.isNullOrEmpty()) return "zip"
        return s.substring(s.lastIndexOf("/") + 1)
    }

}
