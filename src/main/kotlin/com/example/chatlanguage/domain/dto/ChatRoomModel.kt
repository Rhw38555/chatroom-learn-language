package com.example.chatlanguage.domain.dto

import com.example.chatlanguage.domain.entity.ChatRoom
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import javax.persistence.Id


data class ChatRoomListResponseDto(
    val chatRoomList: List<ChatRoomResponseDto>,
)

data class ChatRoomResponseDto(
    val id: String,
    val userId: String,
    val language: String,
    val level: String,
    val scenarioKor: String,
    val scenarioEng: String,
    val myRoleKor: String,
    val myRoleEng: String,
    val gptRoleKor: String,
    val gptRoleEng: String,
    val createdAt: String,
)

data class ChatRoomCreateRequestDto(
    val language: String,
    val level: String,
    val scenarioKor: String,
    val scenarioEng: String,
    val myRoleKor: String,
    val myRoleEng: String,
    val gptRoleKor: String,
    val gptRoleEng: String,
)

data class ChatMessageListResponseDto(
    val chatMessageList : List<ChatMessageResponseDto>,
)

data class ChatMessageResponseDto(
    val message: String,
    val sender: String, // 'me' or 'gpt'
    val chatRoomId: String,
    var createdAt: String,
)

data class ChatMessageRequestDto(
    val message: String,
    val sender: String, // 'me' or 'gpt'
    val chatRoomId: String,
)

data class CommentRecommendRequestDto(
    val chatRoomId: String,
)