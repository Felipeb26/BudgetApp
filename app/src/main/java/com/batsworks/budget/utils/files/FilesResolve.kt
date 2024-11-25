package com.batsworks.budget.utils.files

import android.content.Context
import android.net.Uri
import android.util.Log
import java.net.URLConnection


fun getFileType(context: Context, uri: Uri): String {
	return try {
		Log.d("TPath", uri.path ?: "vazio p")
		Log.d("THost", uri.host ?: "vazio h")

		val finalURI = Uri.parse(Uri.decode(uri.toString()))
		val `is` = context.contentResolver.openInputStream(finalURI)
		val mimeType = URLConnection.guessContentTypeFromStream(`is`)
		if (mimeType.isNullOrBlank()) extension(finalURI.path) else mimeType
	} catch (e: Exception) {
		Log.d("FILE_TYPE", e.message ?: "erro ao ver tipo do arquivo")
		"break"
	}
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
