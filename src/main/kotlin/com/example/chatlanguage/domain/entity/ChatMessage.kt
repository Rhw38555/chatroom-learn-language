package com.example.chatlanguage.domain.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Document
data class ChatMessage (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String? = null,

    @Field("message")
    val message: String,

    @Field("sender")
    val sender: String, // 'me' or 'gpt'

    val chatRoomId: String,

    @Field("created_date")
    @CreatedDate
    var createdAt: LocalDateTime? = null,

)