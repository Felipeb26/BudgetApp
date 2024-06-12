package com.batsworks.budget.language

import android.content.Context
import android.content.ContextWrapper
import java.util.Locale

class LanguageHelper {

	companion object {
		fun changeLanguage(context: Context, languageCode: String): ContextWrapper {
			var contexts = context
			val resources = context.resources
			val configuration = resources.configuration
			val systemLocales = configuration.locales[0]

			if (languageCode.isNotEmpty() && languageCode != systemLocales.language) {
				var locale = Locale(languageCode)
				Locale.setDefault(locale)
				configuration.setLocale(locale)
				contexts = contexts.createConfigurationContext(configuration)
			}
			return ContextWrapper(contexts)
		}
	}

}