package com.example.chatlanguage.service

import com.example.chatlanguage.domain.dto.ChatMessageListResponseDto
import com.example.chatlanguage.domain.dto.ChatMessageRequestDto
import com.example.chatlanguage.domain.dto.ChatMessageResponseDto
import com.example.chatlanguage.domain.dto.CommentRecommendRequestDto
import com.example.chatlanguage.domain.entity.ChatMessage
import com.example.chatlanguage.domain.repository.ChatMessageRepository
import com.example.userservice.exception.NotRecommendException
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ChatMessageService @Autowired constructor(
    private val chatRoomService: ChatRoomService,
    private val chatMessageRepository: ChatMessageRepository,
    private val gptService: MyChatGptService,
){

    fun countByChatRoomId(chatRoomId: String) : Long{
        return chatMessageRepository.countByChatRoomId(chatRoomId)
    }

    fun findAllChatMessage(chatRoomId: String) : ChatMessageListResponseDto{
        return ChatMessageListResponseDto(chatMessageRepository.findTop10ByChatRoomIdOrderByCreatedAtAsc(chatRoomId).map{
                ChatMessageResponseDto(
                    message = it.message, sender = it.sender, createdAt = it.createdAt!!.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    chatRoomId = it.chatRoomId
                )
        })
    }

    fun createChatMessage(chatMessageRequestDto: ChatMessageRequestDto) : ChatMessageResponseDto{
        val chat = chatMessageRequestDto.run {
            ChatMessage(
                message = message, sender = sender, chatRoomId = chatRoomId, createdAt = LocalDateTime.now(),
            )
        }

        return chatMessageRepository.save(chat).run {
            ChatMessageResponseDto(
                message = message, sender = sender, chatRoomId = chatRoomId, createdAt = createdAt!!.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        }
    }

    fun commentRecommend(commentRecommendRequestDto: CommentRecommendRequestDto): String? {
        val chatRoomEntity = chatRoomService.findById(commentRecommendRequestDto.chatRoomId) ?: throw NotRecommendException()
        val chatMessageEntity = chatMessageRepository.findTop1ByChatRoomIdAndSenderOrderByCreatedAtDesc(commentRecommendRequestDto.chatRoomId, "gpt")?: throw NotRecommendException()
        val gptRequest = listOf(MultiChatMessage("system", """
        You have been a helpful assistant in supporting people learning $chatRoomEntity.language.
        Your name was a role-playing bot.
        I assumed the user you are supporting is $chatRoomEntity.level in $chatRoomEntity.language.
        
        I chatted with $chatRoomEntity.language and only responded with $chatRoomEntity.language.
        without providing a translation. And don't write down the pronunciation.
        I assumed the situation of $chatRoomEntity.scenarioEng.
        I was $chatRoomEntity.myRoleEng and your character was $chatRoomEntity.gptRoleEng.
        Use words at the $chatRoomEntity.language and $chatRoomEntity.level levels as much as possible.
        . There were previously forwarded messages and the last one is $chatMessageEntity.message.
        Based on the last message of this situation play, please recommend the following message.
        Suggested message when sending a message to me: Deliver it in the form of a "situational drama message".
        And deliver it the same as the last message language type I delivered.
        """.trimIndent()),
            MultiChatMessage("user","Suggest a situational message based on the last message."))
        return gptService.generateResponse(gptRequest)
    }
}
