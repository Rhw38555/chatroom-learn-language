package com.example.chatlanguage.domain.repository

import com.example.chatlanguage.domain.entity.ChatRoom
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : MongoRepository<ChatRoom, String> {
    fun findAllByUserId(userId: String): List<ChatRoom>
}