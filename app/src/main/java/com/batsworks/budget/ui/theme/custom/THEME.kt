package com.batsworks.budget.ui.theme.custom

enum class THEME(val theme: String) {
    THANOS("THANOS"),
    NATURE("NATURE"),
    CHERRY("CHERRY"),
    COLD("COLD")
}

val themes =  listOf(THEME.COLD.theme, THEME.NATURE.theme, THEME.CHERRY.theme, THEME.THANOS.theme)
