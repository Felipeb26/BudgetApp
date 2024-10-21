package com.batsworks.budget.ui.theme.custom

import android.app.Activity
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.batsworks.budget.ui.theme.Color100
import com.batsworks.budget.ui.theme.Color200
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color900
import com.batsworks.budget.ui.theme.Color950
import com.batsworks.budget.ui.theme.ColorCardCartoes
import com.batsworks.budget.ui.theme.ColorCardEmprestimo
import com.batsworks.budget.ui.theme.ColorCardInvestimentos

class CustomTheme(view: View, val theme: THEME = THEME.CHERRY) {

    init {
        when (theme) {
            THEME.COLD -> coldTheme()
            THEME.NATURE -> natureTheme()
            THEME.THANOS -> thanosTheme()
            THEME.CHERRY -> cherryTheme()
        }

        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            window.statusBarColor = Color800.toArgb()
            window.navigationBarColor = Color700.toArgb()
            WindowCompat.getInsetsController(window, view)
        }
    }

    fun findTheme(themeSelected: String?) {
        val theme = com.batsworks.budget.ui.theme.custom.findTheme(themeSelected)
        when (theme) {
            THEME.COLD -> coldTheme()
            THEME.NATURE -> natureTheme()
            THEME.THANOS -> thanosTheme()
            THEME.CHERRY -> cherryTheme()
        }
    }

    private fun thanosTheme() {
        Color50 = Color(0xFFf3f2fb)
        Color100 = Color(0xFFeae8f7)
        Color200 = Color(0xFFd8d5f0)
        Color300 = Color(0xFFc0bae7)
        Color400 = Color(0xFFaa9edb)
        Color500 = Color(0xFF532E86)
        Color600 = Color(0xFF8b6dbe)
        Color700 = Color(0xFF785ca6)
        Color800 = Color(0xFF5c487f)
        Color900 = Color(0xFF2f273f)
        Color950 = Color(0xFF51426d)

        ColorCardEmprestimo = Color(0xFFFF00FF)
        ColorCardCartoes = Color(0xFFE6E6FA)
        ColorCardInvestimentos = Color(0xFFADD8E6)
    }

    private fun coldTheme() {
        Color50 = Color(0xFFF2F7FB)
        Color100 = Color(0xFFe7f0f8)
        Color200 = Color(0xFFd3e2f2)
        Color300 = Color(0xFFb9cfe8)
        Color400 = Color(0xFF9cb6dd)
        Color500 = Color(0xFF485B81)
        Color600 = Color(0xFF6a7fc1)
        Color700 = Color(0xFF6374ae)
        Color800 = Color(0xFF4a5989)
        Color900 = Color(0xFF414e6e)
        Color950 = Color(0xFF262c40)

        ColorCardEmprestimo = Color(0xFFFF8C00)
        ColorCardCartoes = Color(0xFFD3D3D3)
        ColorCardInvestimentos = Color(0xFF008080)
    }

    private fun natureTheme() {
        Color50 = Color(0xFFf3faf6)
        Color100 = Color(0xFFd8efe4)
        Color200 = Color(0xFFb1dec9)
        Color300 = Color(0xFF64af92)
        Color400 = Color(0xFF64af92)
        Color500 = Color(0xFF296D38)
        Color600 = Color(0xFF3e8e70)
        Color700 = Color(0xFF2a5b4b)
        Color800 = Color(0xFF254a3e)
        Color900 = Color(0xFF223f35)
        Color950 = Color(0xFF0f241e)

        ColorCardEmprestimo = Color(0xFF8B4513)
        ColorCardCartoes = Color(0xFFFFD700)
        ColorCardInvestimentos = Color(0xFFF5F5DC)
    }

    private fun cherryTheme() {
        Color50 = Color(0xFFfff0f0)
        Color100 = Color(0xFFffdddd)
        Color200 = Color(0xFFffc0c0)
        Color300 = Color(0xFFff9494)
        Color400 = Color(0xFFff5757)
        Color500 = Color(0xFF413B3B)
        Color600 = Color(0xFFB45F5F)
        Color700 = Color(0xFF413B3B)
        Color800 = Color(0xFFF16868)
        Color900 = Color(0xFF9E5B5B)
        Color950 = Color(0xFF500000)

        ColorCardEmprestimo = Color(0xFF800000)
        ColorCardCartoes = Color(0xFFFFD700)
        ColorCardInvestimentos = Color(0xFF228B22)
    }

}

fun findTheme(theme: String?): THEME {
    return try {
        THEME.valueOf(theme ?: THEME.CHERRY.theme)
    } catch (e: Exception) {
        THEME.CHERRY
    }
}