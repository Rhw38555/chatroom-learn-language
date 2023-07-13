package com.example.chatlanguage.domain.dto

import java.time.LocalDateTime

data class UserCreateResponseDto(
    val username: String,
    val createdAt: String?,
)

data class UserCreateRequestDto(
    val username: String,
    val password: String,
)

data class UserLoginRequestDto(
    val username: String,
    val password: String,
)

