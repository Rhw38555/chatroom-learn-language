package com.example.chatlanguage.enum

enum class LanguageType(val value: String) {
    KOREAN("Korean"),
    ENGLISH("English"),
    JAPANESE("Japanese"),
    CHINESE("Chinese"),
    SPANISH("Spanish"),
    GERMAN("German"),
    RUSSIAN("Russian");

    companion object {
        fun fromValue(value: String): LanguageType? = values().find { it.value == value }
    }
}