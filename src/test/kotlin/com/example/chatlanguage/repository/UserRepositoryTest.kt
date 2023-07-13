package com.example.chatlanguage.repository

import com.example.chatlanguage.domain.entity.User
import com.example.chatlanguage.domain.repository.UserRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
class UserRepositoryTest : StringSpec() {
    private val userRepository = mockk<UserRepository>()

    init {
        "특정 username으로 사용자를 저장하고 조회할 수 있어야 합니다" {
            val user = User(id = null, username = "john_doe", password = "password")
            every { userRepository.findByUsername("john_doe") } returns user

            val foundUser = userRepository.findByUsername("john_doe")
            foundUser shouldBe user
        }

        "사용자가 존재하지 않을 때 null을 반환해야 합니다" {
            every { userRepository.findByUsername("non_existing_user") } returns null

            val foundUser = userRepository.findByUsername("non_existing_user")
            foundUser shouldBe null
        }

        // Add more test cases...
    }
}

