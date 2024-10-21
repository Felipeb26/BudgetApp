package com.batsworks.budget.components.icons

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.content.res.ResourcesCompat
import com.batsworks.budget.R

@Composable
fun visibilityIsOn(on: Boolean): ImageVector {
    return if (on) {
        ImageVector.vectorResource(R.drawable.ic_visibility)
    } else {
        ImageVector.vectorResource(R.drawable.ic_visibility_off)
    }
}

fun visibilityIsOn(on: Boolean, context: Context): Drawable {
    val resources = context.resources
    val theme = context.theme
    return if (on) {
        ResourcesCompat.getDrawable(resources, R.drawable.ic_visibility, theme)!!
    } else {
        ResourcesCompat.getDrawable(resources, R.drawable.ic_visibility_off, theme)!!
    }
}