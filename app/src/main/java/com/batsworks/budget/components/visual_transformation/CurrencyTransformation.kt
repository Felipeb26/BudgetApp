package com.batsworks.budget.components.visual_transformation

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.batsworks.budget.components.AJUST_TAG

class CurrencyTransformation : VisualTransformation {

    private val tag = CurrencyTransformation::class.java.name
    override fun filter(text: AnnotatedString): TransformedText {

        val currencyMask = text.text.mapIndexed { index, c ->
            when (index) {
                0 -> "R$ $c"
                else -> c
            }
        }.joinToString(separator = "")

        return try {
            TransformedText(AnnotatedString(currencyMask), CurrencyOffset)
        } catch (e: Exception) {
            Log.d(AJUST_TAG(tag), e.message ?: "error has happen")
            TransformedText(text, CurrencyOffset)
        }
    }

    object CurrencyOffset : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset > 1 -> offset + 3
                else -> offset
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset > 1 -> offset - 3
                else -> offset
            }
        }

    }
}