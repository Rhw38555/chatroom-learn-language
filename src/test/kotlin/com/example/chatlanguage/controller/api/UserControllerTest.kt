package com.example.chatlanguage.controller.api

import com.example.chatlanguage.domain.dto.UserCreateRequestDto
import com.example.chatlanguage.domain.dto.UserCreateResponseDto
import com.example.chatlanguage.service.UserService
import com.example.userservice.exception.UsernameNotMatchedException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


class UserControllerTest : BehaviorSpec({
    val userService = mockk<UserService>()
    val userController = UserController(userService)

    given("정상 유저 정보가 주어졌을 때"){
        val userCreateRequestDto = UserCreateRequestDto(username = "john", password = "password")
        val createdUser = UserCreateResponseDto(username = "john", createdAt = "2023-07-05 10:00:00")

        every { userService.createUser(userCreateRequestDto) } returns createdUser

        `when`("회원가입을 호출한다"){
            val result: ResponseEntity<UserCreateResponseDto> = userController.signup(userCreateRequestDto)
            then("HTTP 상태코드가 201과 생성된 사용자 정보를 반환한다."){
                result.statusCode shouldBe HttpStatus.CREATED
                result.body shouldBe createdUser
            }
        }
    }

    given("비정상 유저 정보가 주어졌을 때"){
        val userCreateRequestDto = UserCreateRequestDto(username = "", password = "")
        `when`("회원가입을 호출한다"){
            every { userService.createUser(userCreateRequestDto) } throws UsernameNotMatchedException("사용자명이 잘못되었습니다.")
            val exception = shouldThrow<UsernameNotMatchedException> {
                userController.signup(userCreateRequestDto)
            }
            then("에러메세지를 반환한다."){
                Then("UsernameNotMatchedException 발생해야 합니다") {
                    exception.message shouldBe "사용자명이 잘못되었습니다."
                }
            }
        }
    }
})