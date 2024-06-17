package com.batsworks.budget.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.decodeBitmap
import com.batsworks.budget.R
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


fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val width = if (!drawable.bounds.isEmpty) drawable.bounds.width() else drawable.intrinsicWidth

    val height =
        if (!drawable.bounds.isEmpty) drawable.bounds.height() else drawable.intrinsicHeight

    val bitmap = Bitmap.createBitmap(
        if (width <=
            0
        ) 1
        else width, if (height <=
            0
        ) 1
        else height, Bitmap.Config.ARGB_8888
    )
    val canvas: Canvas = Canvas(bitmap)
    drawable.setBounds(
        0,
        0,
        canvas.width, canvas.height
    )
    drawable.draw(canvas)

    return bitmap
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