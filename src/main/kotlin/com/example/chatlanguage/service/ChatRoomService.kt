package com.example.chatlanguage.service

import com.example.chatlanguage.domain.dto.ChatRoomCreateRequestDto
import com.example.chatlanguage.domain.dto.ChatRoomListResponseDto
import com.example.chatlanguage.domain.dto.ChatRoomResponseDto
import com.example.chatlanguage.domain.entity.ChatRoom
import com.example.chatlanguage.domain.repository.ChatRoomRepository
import com.example.chatlanguage.enum.LanguageType
import com.example.chatlanguage.enum.LevelType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ChatRoomService @Autowired constructor(
    private val chatRoomRepository: ChatRoomRepository,
) {
    fun findById(roomId: String) : ChatRoomResponseDto{
        return chatRoomRepository.findById(roomId).get().run {
            ChatRoomResponseDto(id=id!!, userId = userId, language=language.value, level=level.value,
            scenarioKor=scenarioKor, scenarioEng=scenarioEng, myRoleKor=myRoleKor, myRoleEng=myRoleEng,
            gptRoleKor=gptRoleKor, gptRoleEng=gptRoleEng, createdAt = createdAt!!.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        }
    }

    fun findAllChatRoom(userid : String) : ChatRoomListResponseDto{
        return ChatRoomListResponseDto(chatRoomRepository.findAllByUserId(userid).map{
            ChatRoomResponseDto(
                id = it.id.toString(),
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
    }

    fun createRoom(chatRoomCreateRequestDto: ChatRoomCreateRequestDto, userId: String): ChatRoomResponseDto {
        val chatRoom = chatRoomCreateRequestDto.run {
            ChatRoom(userId=userId, language = LanguageType.fromValue(language)!!, level = LevelType.fromValue(level)!!, scenarioKor = scenarioKor,
                scenarioEng = scenarioEng, myRoleKor = myRoleKor, myRoleEng = myRoleEng, gptRoleKor = gptRoleKor, gptRoleEng = gptRoleEng, createdAt = LocalDateTime.now(),
            )
        }

        return chatRoomRepository.save(chatRoom)
            .run{
                ChatRoomResponseDto(id=id!!, userId=userId, language=language.value, level=level.value, scenarioKor=scenarioKor,
                scenarioEng=scenarioEng, myRoleKor=myRoleKor, myRoleEng=myRoleEng, gptRoleKor=gptRoleKor, gptRoleEng = gptRoleEng,
                    createdAt = createdAt!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        }
    }
}