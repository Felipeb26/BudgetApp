package com.batsworks.budget.components.visual_transformation

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyTransformation : VisualTransformation {

    private val tag = CurrencyTransformation::class.java.name
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val prefix = "R$ "

        val currencyMask = if (originalText.isNotEmpty()) {
            "$prefix$originalText"
        } else {
            prefix
        }

        return try {
            TransformedText(AnnotatedString(currencyMask), CurrencyOffset(prefix.length))
        } catch (e: Exception) {
            Log.d(tag, e.message ?: "An error has occurred")
            TransformedText(text, CurrencyOffset(prefix.length))
        }
    }

    class CurrencyOffset(private val prefixLength: Int) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset + prefixLength
        }

        override fun transformedToOriginal(offset: Int): Int {
            return if (offset < prefixLength) 0   else offset - prefixLength
        }
    }
}