package com.batsworks.budget.utils.files.zip

import android.content.Context
import android.net.Uri
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

fun zipFile(context: Context, uri: Uri, fileName: String?): ByteArray {
    val contentResolver = context.contentResolver
    val baos = ByteArrayOutputStream()
    val stream = contentResolver.openInputStream(uri)
    if (stream != null) {
        return writeToZip(stream, fileName)
    }
    return baos.toByteArray()
}


fun writeToZip(fis: InputStream, fileName: String?): ByteArray {
    val buffer = ByteArray(1024)
    val baos = ByteArrayOutputStream()
    val name = if (fileName.isNullOrEmpty()) UUID.randomUUID().toString()
    else fileName

    ZipOutputStream(baos).use { zipOut ->
        zipOut.putNextEntry(ZipEntry("$name.pdf"))
        var bytesRead: Int
        while (fis.read(buffer).also { bytesRead = it } != -1) {
            zipOut.write(buffer, 0, bytesRead)
        }
        zipOut.closeEntry()
    }
    fis.close()
    return baos.toByteArray()
}


fun decompressData(compressedData: ByteArray): ByteArray {
    ByteArrayInputStream(compressedData).use { bais ->
        ZipInputStream(bais).use { zipIn ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            val baos = ByteArrayOutputStream()

            zipIn.nextEntry
            while (zipIn.read(buffer).also { bytesRead = it } != -1) {
                baos.write(buffer, 0, bytesRead)
            }
            zipIn.closeEntry()
            return baos.toByteArray()
        }
    }
}