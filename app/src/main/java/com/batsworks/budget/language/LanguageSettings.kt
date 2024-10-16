package com.batsworks.budget.language

import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.batsworks.budget.R

class LanguageSettings(private val configuration: Configuration) {

	val enabled = mapOf(
		R.string.en to "en",
		R.string.ptBR to "pt-BR"
	)

	fun selectAppLanguage(index: Int) {
		Log.d("TES", "${enabled[index]}")
		AppCompatDelegate.setApplicationLocales(
			LocaleListCompat.forLanguageTags(enabled[index])
		)
	}

	fun current(): String {
		return format(configuration.locales[0].language)
	}

	private fun format(language: String): String {
		return when (language) {
			"pt" -> "portugues"
			"en" -> "English"
			else -> "unkonow for the app"
		}
	}

}