package com.batsworks.budget.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.batsworks.budget.R

class LanguageSettings {

	val enabled = mapOf(
		R.string.en to "en",
		R.string.ptBR to "pt-BR"
	)

	fun selectAppLanguage(index: Int) {
		AppCompatDelegate.setApplicationLocales(
			LocaleListCompat.forLanguageTags(enabled[index])
		)
	}

}