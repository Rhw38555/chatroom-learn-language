package com.example.chatlanguage.service

import com.example.chatlanguage.domain.dto.UserCreateRequestDto
import com.example.chatlanguage.domain.dto.UserLoginRequestDto
import com.example.chatlanguage.domain.entity.User
import com.example.chatlanguage.domain.repository.UserRepository
import com.example.userservice.exception.PasswordNotMatchedException
import com.example.userservice.exception.UsernameNotMatchedException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserServiceTest : BehaviorSpec({
    val userRepository = mockk<UserRepository>()
    val passwordEncoder = mockk<BCryptPasswordEncoder>()
    val userService = UserService(userRepository, passwordEncoder)


    Given("사용자 생성 요청 데이터가 주어졌을 때") {
        val userCreateDto = UserCreateRequestDto(username = "john_doe", password = "password")
        val encodedPassword = "encodedPassword"
        val user = User(id = null, username = userCreateDto.username, password = encodedPassword)
        user.createdAt = LocalDateTime.now()

        every { userRepository.findByUsername(userCreateDto.username) } returns null
        every { passwordEncoder.encode(userCreateDto.password) } returns encodedPassword
        every { userRepository.save(user) } returns user

        When("새로운 사용자를 생성할 때 유저가 존재하지 않으면") {
            val result = userService.createUser(userCreateDto)

            Then("사용자가 성공적으로 생성되어야 합니다") {
                result.username shouldBe user.username
                result.createdAt shouldBe user.createdAt!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }

            Then("사용자의 비밀번호가 암호화되어야 합니다") {
                verify(exactly = 1) { passwordEncoder.encode(userCreateDto.password) }
            }

            Then("사용자가 저장소에 저장되어야 합니다") {
                verify(exactly = 1) { userRepository.save(user) }
            }
        }
    }

    Given("사용자 생성 요청 데이터가 잘못된 경우") {
        val userCreateDto = UserCreateRequestDto(username = "", password = "")


        When("새로운 사용자를 생성할 때") {
            val exception = shouldThrow<UsernameNotMatchedException> {
                userService.createUser(userCreateDto)
            }

            Then("UsernameNotMatchedException 발생해야 합니다") {
                exception.message shouldBe "사용자명이 잘못되었습니다."
            }

            Then("사용자의 비밀번호는 암호화되지 않아야 합니다") {
                verify(exactly = 0) { passwordEncoder.encode(any()) }
            }

            Then("사용자는 저장소에 저장되지 않아야 합니다") {
                verify(exactly = 0) { userRepository.save(any()) }
            }
        }
    }

    Given("사용자 비 정상 정보가 입력 되었을때"){
        When("사용자 로그인 할 때"){

            val exception = shouldThrow<UsernameNotMatchedException> {
                userService.loadUserByUsername("")
            }
            Then("UsernameNotMatchedException 발생해야 합니다"){
                exception.message shouldBe "사용자명이 잘못되었습니다."
            }
        }
    }

    Given("패스워드가 정상 입력 되었을때"){
        val user = User(username = "user1", password = "test1")
        val userPrinc = org.springframework.security.core.userdetails.User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(SimpleGrantedAuthority("USER"))
            .build();

        every { userRepository.findByUsername("user1") } returns user

        When("사용자 로그인 할 때"){

            val result = userService.loadUserByUsername("user1")

            Then("로그인이 정상 동작해야 합니다"){
                result shouldBe userPrinc
            }
        }
    }

})