package com.example.chatlanguage.service

import com.example.chatlanguage.domain.dto.ChatMessageRequestDto
import com.example.chatlanguage.domain.dto.ChatMessageResponseDto
import com.example.chatlanguage.domain.dto.ChatRoomResponseDto
import com.example.chatlanguage.domain.dto.CommentRecommendRequestDto
import com.example.chatlanguage.domain.entity.ChatMessage
import com.example.chatlanguage.domain.entity.ChatRoom
import com.example.chatlanguage.domain.repository.ChatMessageRepository
import com.example.chatlanguage.enum.LanguageType
import com.example.chatlanguage.enum.LevelType
import com.example.userservice.exception.NotRecommendException
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatServiceTest : DescribeSpec({
    val chatMessageRepository = mockk<ChatMessageRepository>()
    val chatRoomService = mockk<ChatRoomService>()
    val gptService = mockk<MyChatGptService>()
    val chatMessageService = ChatMessageService(chatRoomService, chatMessageRepository, gptService)

    describe("findAllChatMessage 함수") {
        it("주어진 채팅방 ID에 해당하는 채팅 메시지 목록을 반환해야 함") {
            val chatRoomId = "chatRoomId"
            val expectedChatMessages = listOf(
                ChatMessage("1","안녕하세요", "Sender1", chatRoomId, LocalDateTime.now()),
                ChatMessage("2","반갑습니다", "Sender2", chatRoomId, LocalDateTime.now())
            )
            every { chatMessageRepository.findTop10ByChatRoomIdOrderByCreatedAtAsc(chatRoomId) } returns expectedChatMessages

            val result = chatMessageService.findAllChatMessage(chatRoomId)

            result.chatMessageList shouldBe expectedChatMessages.map {
                ChatMessageResponseDto(
                    message = it.message,
                    sender = it.sender,
                    chatRoomId = it.chatRoomId,
                    createdAt = it.createdAt!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                )
            }
        }
    }

    describe("createChatMessage 함수") {
        it("새로운 채팅 메시지를 저장하고 응답으로 반환해야 함") {
            val chatMessageRequestDto = ChatMessageRequestDto("안녕하세요", "Sender1", "chatRoomId")
            val expectedSavedChatMessage = ChatMessage(
                "1",
                chatMessageRequestDto.message,
                chatMessageRequestDto.sender,
                chatMessageRequestDto.chatRoomId,
                LocalDateTime.now()
            )
            val expectedResponseDto = ChatMessageResponseDto(
                expectedSavedChatMessage.message,
                expectedSavedChatMessage.sender,
                expectedSavedChatMessage.chatRoomId,
                expectedSavedChatMessage.createdAt!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            every { chatMessageRepository.save(any()) } returns expectedSavedChatMessage

            val result = chatMessageService.createChatMessage(chatMessageRequestDto)

            result shouldBe expectedResponseDto
        }
    }

    describe("commentRecommend 함수") {
        it("새로운 채팅 메시지를 추천") {
            val commentRecommendRequestDto = CommentRecommendRequestDto("asdfdsafsdafasd")
            val commentRecommend = "추천 받은 메세지 입니다."
            val chatRoom = ChatRoom(
                id="asdfdsafsdafasd", userId = "test", language = LanguageType.KOREAN, level = LevelType.BEGINNER,
                scenarioKor = "뉴욕 시내 스타벅스에서 커피 주문하기", scenarioEng = "Ordering White at Gall Starbucks in New York",
                myRoleKor = "커피 주문 고객", myRoleEng = "customer ordering coffee",
                gptRoleKor = "스타벅스 직원", gptRoleEng = "Starbucks employee",
                createdAt = LocalDateTime.now(),
            )
            val chatRoomResponse = with(chatRoom) {
                ChatRoomResponseDto(id=id!!, userId = userId, language=language.value, level=level.value,
                    scenarioKor=scenarioKor, scenarioEng=scenarioEng, myRoleKor=myRoleKor, myRoleEng=myRoleEng,
                    gptRoleKor=gptRoleKor, gptRoleEng=gptRoleEng, createdAt = createdAt!!.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            }

            every { chatRoomService.findById(commentRecommendRequestDto.chatRoomId) } returns chatRoomResponse
            every {chatMessageRepository
                .findTop1ByChatRoomIdAndSenderOrderByCreatedAtDesc(commentRecommendRequestDto.chatRoomId, "gpt")} returns ChatMessage(message="test", sender ="me", chatRoomId = "asdfdsafsdafasd")
            every { gptService.generateResponse(any()) } returns commentRecommend

            val result = chatMessageService.commentRecommend(commentRecommendRequestDto)

            result shouldBe commentRecommend
        }
    }
})