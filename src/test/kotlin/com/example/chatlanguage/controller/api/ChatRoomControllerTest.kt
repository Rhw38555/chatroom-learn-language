package com.example.chatlanguage.controller.api

import com.example.chatlanguage.domain.dto.ChatRoomCreateRequestDto
import com.example.chatlanguage.domain.dto.ChatRoomListResponseDto
import com.example.chatlanguage.domain.dto.ChatRoomResponseDto
import com.example.chatlanguage.domain.entity.ChatRoom
import com.example.chatlanguage.enum.LanguageType
import com.example.chatlanguage.enum.LevelType
import com.example.chatlanguage.service.ChatRoomService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.security.Principal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRoomControllerTest: BehaviorSpec({

    val chatRoomService = mockk<ChatRoomService>()
    val chatRoomController = ChatRoomController(chatRoomService)

    Given("사용자 정보가 전달될 때"){
        val userId = "test1"
        val chatRoom = ChatRoom(
            userId = userId, language = LanguageType.KOREAN, level = LevelType.BEGINNER,
            scenarioKor = "뉴욕 시내 스타벅스에서 커피 주문하기", scenarioEng = "Ordering White at Gall Starbucks in New York",
            myRoleKor = "커피 주문 고객", myRoleEng = "customer ordering coffee",
            gptRoleKor = "스타벅스 직원", gptRoleEng = "Starbucks employee",
            createdAt = LocalDateTime.now(),
        )
        val chatRoomList = listOf(chatRoom)
        val chatRoomResponseList = ChatRoomListResponseDto(chatRoomList.map{
            ChatRoomResponseDto(
                id="123123123123",
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

        val chatResponseEntity = ResponseEntity.status(HttpStatus.OK)
            .body(chatRoomResponseList)

        every { chatRoomService.findAllChatRoom(userId) } returns chatRoomResponseList

        When("채팅방 정보가 호출된다"){
            val result = chatRoomController.getChatRoomsByUserId(userId)
            Then("채팅방 리스트가 호출된다"){
                result.body!!.chatRoomList.get(0).scenarioKor shouldBe chatRoomResponseList.chatRoomList.get(0).scenarioKor
            }
        }
    }

    Given("사용자 정보가 전달되면"){
        val userId = "test1"
        val chatRoom = ChatRoom(
            userId = userId, language = LanguageType.KOREAN, level = LevelType.BEGINNER,
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
                id="123123123123",
                userId = userId,
                language = chatRoom.language.value,
                level = chatRoom.level.value,
                scenarioKor = scenarioKor,
                scenarioEng = scenarioEng,
                myRoleKor = myRoleKor,
                myRoleEng = myRoleEng,
                gptRoleEng = gptRoleEng,
                gptRoleKor = gptRoleKor,
                createdAt = chatRoom.createdAt!!.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            )
        }


        val chatResponseEntity = ResponseEntity.status(HttpStatus.CREATED)
            .body(chatRoomResponseDto)

        val principal: Principal = mockk()
        every { principal.name } returns "test"

        every { chatRoomService.createRoom(chatRoomCreateRequestDto, userId) } returns chatRoomResponseDto

        When("채팅방이 생성 된다"){
            val result = chatRoomController.getChatRoomsByUserId(userId)
            Then("채팅방이 저장 된다"){
                result.body!!.chatRoomList[0].scenarioKor shouldBe chatResponseEntity.body!!.scenarioKor
            }
        }
    }
})