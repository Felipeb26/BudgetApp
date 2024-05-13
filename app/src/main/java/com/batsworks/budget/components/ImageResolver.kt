package com.batsworks.budget.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.graphics.decodeBitmap
import java.io.ByteArrayOutputStream

fun getImageFromUri(context: Context, uri: Uri): Bitmap {
    return ImageDecoder.createSource(context.contentResolver, uri).decodeBitmap { _, _ -> }
}

fun getByteArrayFromUri(context: Context, uri: Uri): ByteArray {
    val bitmap = ImageDecoder.createSource(context.contentResolver, uri).decodeBitmap { _, _ -> }
    return converterBitmapToByteArray(bitmap)
}

fun converterBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
    return baos.toByteArray()
}

fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}