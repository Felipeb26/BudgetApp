package com.batsworks.budget.components.files.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.decodeBitmap
import com.batsworks.budget.R
import com.batsworks.budget.components.files.getFileType
import com.batsworks.budget.components.files.zipFile
import java.io.ByteArrayOutputStream


fun getImageFromUri(context: Context, uri: Uri): Bitmap {
    return ImageDecoder.createSource(context.contentResolver, uri).decodeBitmap { _, _ -> }
}


fun getByteArrayFromUri(context: Context, uri: Uri, name: String? = null): ByteArray {
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
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
    return baos.toByteArray()
}

fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}


@Composable
fun visibilityIsOn(on: Boolean): ImageVector {
    return if (on) {
        ImageVector.vectorResource(R.drawable.baseline_visibility_24)
    } else {
        ImageVector.vectorResource(R.drawable.baseline_visibility_off_24)
    }
}

fun visibilityIsOn(on: Boolean, context: Context): Drawable {
    val resources = context.resources
    val theme = context.theme
    return if (on) {
        ResourcesCompat.getDrawable(resources, R.drawable.baseline_visibility_24, theme)!!
    } else {
        ResourcesCompat.getDrawable(resources, R.drawable.baseline_visibility_off_24, theme)!!
    }
}