package com.example.chatlanguage.domain.repository

import com.example.chatlanguage.domain.entity.ChatMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : MongoRepository<ChatMessage, String>{
    fun findTop10ByChatRoomIdOrderByCreatedAtAsc(chatRoomId: String): List<ChatMessage>
    fun findTop1ByChatRoomIdAndSenderOrderByCreatedAtDesc(chatRoomId: String, sender: String): ChatMessage
    fun countByChatRoomId(chatRoomId: String) : Long

}