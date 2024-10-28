package com.batsworks.budget.utils.files.image

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.decodeBitmap
import com.batsworks.budget.utils.files.getFileType
import com.batsworks.budget.utils.files.zip.zipFile
import java.io.ByteArrayOutputStream


fun getImageFromUri(context: Context, uri: Uri): Bitmap {
    return ImageDecoder.createSource(context.contentResolver, uri).decodeBitmap { _, _ -> }
}

fun getByteArrayFromUri(context: Context, uri: Uri?, name: String? = null): ByteArray {
    if (uri == null) return byteArrayOf()
    val fileType = getFileType(context, uri)
    Log.d("FILE", fileType)
    return when (fileType) {
        "txt" -> zipFile(context, uri, name)
        "pdf" -> zipFile(context, uri, name)
        else -> compressImage(context, uri)
    }
}

fun getByteArrayFromUri(context: Context, stringUri: String?, name: String? = null): ByteArray {
    if (stringUri == null) return byteArrayOf()
    val uri = Uri.parse(stringUri)
    val fileType = getFileType(context, uri)
    Log.d("FILE", fileType)
    return when (fileType) {
        "txt" -> zipFile(context, uri, name)
        "pdf" -> zipFile(context, uri, name)
        else -> compressImage(context, uri)
    }
}

fun compressImage(context: Context, uri: Uri): ByteArray {
    val bitmap = ImageDecoder.createSource(context.contentResolver, uri).decodeBitmap { _, _ -> }
    return converterBitmapToByteArray(bitmap)
}

fun converterBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val baos = ByteArrayOutputStream()

    val quality = if (bitmap.width > 1000 || bitmap.height > 1000) 50 else 70
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, quality, baos)
    }else{
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
    }
    return baos.toByteArray()
}

fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun getImageUriFromByteArray(
    file: ByteArray,
    contentResolver: ContentResolver,
    title: String? = null,
    description: String? = null,
): Uri? {

    val contentValues = ContentValues()
    title?.let { contentValues.put(MediaStore.Images.Media.TITLE, title) }
    description?.let { contentValues.put(MediaStore.Images.Media.DESCRIPTION, it) }
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