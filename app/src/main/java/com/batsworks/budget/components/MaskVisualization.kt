package com.batsworks.budget.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MaskTransformation(private val range: IntRange) : VisualTransformation {
	override fun filter(text: AnnotatedString): TransformedText {
		return maskFilter(text, range)
	}
}


fun maskFilter(text: AnnotatedString, range: IntRange): TransformedText {

	// NNNNN-NNN
	val trimmed = if (text.text.length >= range.last + 1) text.text.substring(range) else text.text
	var out = ""
	for (i in trimmed.indices) {
		out += trimmed[i]
		if (i == 4) out += "-"
	}

	val numberOffsetTranslator = object : OffsetMapping {
		override fun originalToTransformed(offset: Int): Int {
			if (offset <= ((range.first + range.last) / 2)) return offset
			if (offset <= range.last + 1) return offset + 1
			return range.last + 2

		}

		override fun transformedToOriginal(offset: Int): Int {
			if (offset <= 5) return offset
			if (offset <= 9) return offset - 1
			return 8
		}
	}

	return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}