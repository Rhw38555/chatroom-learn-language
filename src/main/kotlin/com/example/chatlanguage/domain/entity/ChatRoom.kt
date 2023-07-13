package com.example.chatlanguage.domain.entity

import com.example.chatlanguage.enum.LanguageType
import com.example.chatlanguage.enum.LevelType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import javax.persistence.CollectionTable
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Document
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String? = null,

    @Field("user_id")
    val userId: String,

    @Field
    val language: LanguageType,

    @Field
    val level: LevelType,

    @Field("scenario_kor")
    val scenarioKor: String,

    @Field("scenario_eng")
    val scenarioEng: String,

    @Field("my_role_kor")
    val myRoleKor: String,

    @Field("my_role_eng")
    val myRoleEng: String,

    @Field("gpt_role_kor")
    val gptRoleKor: String,

    @Field("gpt_role_eng")
    val gptRoleEng: String,


    @Field("created_date")
    @CreatedDate
    var createdAt: LocalDateTime? = null,

)
