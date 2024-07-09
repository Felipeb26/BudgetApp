package com.batsworks.budget.components.files

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CancellableContinuation
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLConnection
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


fun getFileType(context: Context, uri: Uri): String {
	Log.d("TPath", uri.path ?: "vazio p")
	Log.d("THost", uri.host ?: "vazio h")

	val `is` = context.contentResolver.openInputStream(uri)
	val mimeType = URLConnection.guessContentTypeFromStream(`is`)
	return if (mimeType.isNullOrBlank()) extension(uri.path) else mimeType
}

fun getFileType(bytes: ByteArray?): String {
	if (bytes == null || bytes.isEmpty()) return "empty"
	val fileSignatures = mapOf(
		byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte()) to "jpg",
		byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte()) to "png",
		byteArrayOf(0x47.toByte(), 0x49.toByte(), 0x46.toByte(), 0x38.toByte()) to "gif",
		byteArrayOf(0x25.toByte(), 0x50.toByte(), 0x44.toByte(), 0x46.toByte()) to "pdf",
		byteArrayOf(0x50.toByte(), 0x4B.toByte(), 0x03.toByte(), 0x04.toByte()) to "zip",
		byteArrayOf(0x42.toByte(), 0x4D.toByte()) to "bmp",
		byteArrayOf(0x49.toByte(), 0x44.toByte(), 0x33.toByte()) to "mp3"
	)

	for ((signature, fileType) in fileSignatures) {
		if (bytes.take(signature.size) == signature.toList()) {
			return fileType
		}
	}
	return "unknown"
}

fun extension(name: String?): String {
	if (name.isNullOrEmpty()) return "txt"
	return name.substring(name.lastIndexOf(".") + 1)
}

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


fun downloadFile(caminho: String, bytes: ByteArray, continuation: CancellableContinuation<String>) {
	val file = FileOutputStream(caminho)
	try {
		file.write(bytes)
		file.flush()
		file.close()
		continuation.resume(caminho)
	} catch (e: Exception) {
		file.close()
		continuation.resumeWithException(e)
	}
}

fun getImageUriFromByteArray(
	file: ByteArray,
	contentResolver: ContentResolver,
	title: String? = null,
	description: String? = null,
): Uri? {

	val contentValues = ContentValues()
	title?.let{ contentValues.put(MediaStore.Images.Media.TITLE, title) }
	description?.let { 	contentValues.put(MediaStore.Images.Media.DESCRIPTION, it) }
	contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

	val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

	try {
		val outputStream = contentResolver.openOutputStream(uri!!)
		outputStream!!.write(file)
		outputStream.close()
	} catch (e: Exception) {
		Log.d("IMAGE_TO_URI", e.message ?: "a error has happer", e)
	}
	Log.d("GENERAED_URI", uri.toString())
	return uri
}