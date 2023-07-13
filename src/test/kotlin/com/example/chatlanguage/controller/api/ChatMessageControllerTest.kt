package com.example.chatlanguage.controller.api

import com.example.chatlanguage.domain.dto.ChatMessageListResponseDto
import com.example.chatlanguage.domain.dto.ChatMessageRequestDto
import com.example.chatlanguage.domain.dto.ChatMessageResponseDto
import com.example.chatlanguage.service.ChatMessageService
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.security.Principal

class ChatControllerTest : ShouldSpec() {
    private val chatMessageService = mockk<ChatMessageService>()
    private val chatController = ChatMessageController(chatMessageService)

    init {
        should("Chat RoomId로 채팅목록 조회") {
            val roomId = "chatRoomId"
            val expectedResponse = ChatMessageListResponseDto(listOf(
                ChatMessageResponseDto("Message1", "Sender1", "2023-07-05 10:30:00", roomId),
                ChatMessageResponseDto("Message2", "Sender2", "2023-07-05 11:00:00", roomId)
            ))
            every { chatMessageService.findAllChatMessage(roomId) } returns expectedResponse

            val result = chatController.getChatMessageByChatRoomId(roomId)

            result shouldBe ResponseEntity.status(HttpStatus.OK).body(expectedResponse)
        }

        should("ChatRoomId 기준으로 메세지 생성") {
            val chatMessageRequestDto = ChatMessageRequestDto("Hello", "Sender1", "chatRoomId")
            val expectedResponse = ChatMessageResponseDto("Hello", "Sender1", "chatRoomId", "2023-07-05 12:00:00")
            every { chatMessageService.createChatMessage(chatMessageRequestDto) } returns expectedResponse

            val result = chatController.getChatRoomsByUserId(chatMessageRequestDto)

            result shouldBe ResponseEntity.status(HttpStatus.CREATED).body(expectedResponse)
        }
    }
}