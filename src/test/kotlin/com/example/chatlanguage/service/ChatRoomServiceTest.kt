package com.example.chatlanguage.service

import com.example.chatlanguage.domain.dto.ChatRoomCreateRequestDto
import com.example.chatlanguage.domain.dto.ChatRoomListResponseDto
import com.example.chatlanguage.domain.dto.ChatRoomResponseDto
import com.example.chatlanguage.domain.entity.ChatRoom
import com.example.chatlanguage.domain.repository.ChatRoomRepository
import com.example.chatlanguage.enum.LanguageType
import com.example.chatlanguage.enum.LevelType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRoomServiceTest : BehaviorSpec({

    val chatRoomRepository = mockk<ChatRoomRepository>()
    val chatRoomService = ChatRoomService(chatRoomRepository)

    Given("유저 정보가 전달되었을 때"){
        val userId = "test1"
        val chatRoom = ChatRoom(
            id="asdfdsafsdafasd", userId = userId, language = LanguageType.KOREAN, level = LevelType.BEGINNER,
            scenarioKor = "뉴욕 시내 스타벅스에서 커피 주문하기", scenarioEng = "Ordering White at Gall Starbucks in New York",
            myRoleKor = "커피 주문 고객", myRoleEng = "customer ordering coffee",
            gptRoleKor = "스타벅스 직원", gptRoleEng = "Starbucks employee",
            createdAt = LocalDateTime.now(),
        )
        val chatRoomList = listOf(chatRoom)
        val chatRoomResponseList = ChatRoomListResponseDto(chatRoomList.map{
            ChatRoomResponseDto(
                id = "asdfdsafsdafasd",
                userId = it.userId,
                language = it.language.value,
                level = it.level.value,
                scenarioKor = it.scenarioKor,
                scenarioEng = it.scenarioEng,
                myRoleKor = it.myRoleKor,
                myRoleEng = it.myRoleEng,
                gptRoleEng = it.gptRoleEng,
                gptRoleKor = it.gptRoleKor,
                createdAt = it.createdAt!!.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            )
        })

        When("채팅방 목록을 조회한다"){
            every { chatRoomRepository.findAllByUserId(userId) } returns chatRoomList
            val result = chatRoomService.findAllChatRoom(userId)
            Then("채팅방 목록이 정상 조회 된다."){
                result shouldBe chatRoomResponseList
            }
        }
    }

    Given("채팅방 정보가 입력되었을 때"){
        val userId = "test1"
        val chatRoom = ChatRoom(
            id="123123123123", userId = userId, language = LanguageType.KOREAN, level = LevelType.BEGINNER,
            scenarioKor = "뉴욕 시내 스타벅스에서 커피 주문하기", scenarioEng = "Ordering White at Gall Starbucks in New York",
            myRoleKor = "커피 주문 고객", myRoleEng = "customer ordering coffee",
            gptRoleKor = "스타벅스 직원", gptRoleEng = "Starbucks employee",
            createdAt = LocalDateTime.now(),
        )
        val chatRoomCreateRequestDto = chatRoom.run {
            ChatRoomCreateRequestDto(
                language.value , level.value, scenarioKor, scenarioEng,
                myRoleKor, myRoleEng, gptRoleKor, gptRoleEng
            )
        }

        val chatRoomResponseDto = chatRoomCreateRequestDto.run{
            ChatRoomResponseDto(
                "123123123123", userId, language , level, scenarioKor, scenarioEng,
                myRoleKor, myRoleEng, gptRoleKor, gptRoleEng,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        }

        When("채팅방을 생성한다"){

            every { chatRoomRepository.save(chatRoom) } returns chatRoom
            val result = chatRoomService.createRoom(chatRoomCreateRequestDto, userId)

            Then("채팅방 정보와 입력정보가 같다"){
                result.userId shouldBe chatRoomResponseDto.userId
                result.myRoleEng shouldBe chatRoomResponseDto.myRoleEng
            }
        }
    }

})