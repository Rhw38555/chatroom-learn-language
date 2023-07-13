package com.example.chatlanguage.enum

enum class LevelType(val value: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    companion object {
        fun fromValue(value: String): LevelType? = values().find { it.value == value }
    }
}