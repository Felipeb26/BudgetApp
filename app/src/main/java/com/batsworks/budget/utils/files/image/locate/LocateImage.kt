package com.batsworks.budget.utils.files.image.locate

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmapOrNull


class LocateImage(val context: Context) {

    fun image(res: Int): Bitmap? {
        val resources = context.resources
        val theme = context.theme
        return ResourcesCompat.getDrawable(resources, res, theme)?.toBitmapOrNull()
    }

    fun image(res: Int, width: Double): Bitmap {
        val resources = context.resources
        val theme = context.theme
        val originalBitmap = ResourcesCompat.getDrawable(resources, res, theme)?.toBitmapOrNull()!!

        val reducedWidth = (originalBitmap.width / width).toInt()
        val reducedBitmap = Bitmap.createScaledBitmap(
            originalBitmap,
            reducedWidth,
            originalBitmap.height,
            true
        )
        return reducedBitmap
    }
}
