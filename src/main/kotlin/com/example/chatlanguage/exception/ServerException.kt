package com.example.userservice.exception

sealed class ServerException (
    val code: Int,
    override val message: String,
) : RuntimeException(message)

data class UserExistException(
    override val message: String = "이미 존재하는 유저입니다.",
) : ServerException(409, message)

data class NotFoundUserException(
    override val message: String = "존재하지 않는 유저입니다.",
) : ServerException(404, message)


data class UsernameNotMatchedException(
    override val message: String = "사용자명이 잘못되었습니다.",
) : ServerException(400, message)

data class PasswordNotMatchedException(
    override val message: String = "패스워드가 잘못되었습니다.",
) : ServerException(400, message)

data class InvalidJwtTokenException(
    override val message: String = "잘못된 토큰입니다."
) : ServerException(400, message)

data class NotRecommendException(
    override val message: String = "추천할 수 없습니다."
) : ServerException(500, message)