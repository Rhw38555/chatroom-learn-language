package com.example.chatlanguage.handler

import com.example.chatlanguage.domain.dto.ChatMessageRequestDto
import com.example.chatlanguage.service.MyChatGptService
import com.example.chatlanguage.service.ChatMessageService
import com.example.chatlanguage.service.ChatRoomService
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage
import org.json.JSONObject
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler



@Component
class ChatWebSocketHandler(private val chatRoomService:  ChatRoomService,
                            private val chatMessageService:  ChatMessageService,
                           private val chatGptService: MyChatGptService,
                           ) : TextWebSocketHandler() {


    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {

        // roomId와 textMessage 필드를 추출합니다.
        val jsonMessage = JSONObject(message.payload)
        val roomId = jsonMessage.getString("roomId")
        val textMessage = jsonMessage.getString("textMessage")
        val count = chatMessageService.countByChatRoomId(roomId)
        val room = chatRoomService.findById(roomId)
        val language = room.language
        val level = room.level
        val scenarioEng = room.scenarioEng
        val myRoleEng = room.myRoleEng
        val gptRoleEng = room.gptRoleEng
        val prompt = if(count == 0L){

            """
            You are helpful assistant supporting people learning $language.
            Your name is RolePlayingBot.
            Please assume that the user you are assisting is a $level in $language.
            And please write only the sentence without the character role.

            Let's have a conversation in $language. Please answer in $language only
            without providing a translation. And please don't write down the pronunciation either.
            Let us assume that the situation in $scenarioEng.
            I am $myRoleEng. The character I want you to act as is $gptRoleEng.
            Please make sure that I'm a $level in $language, so please use $level words
            as much as possible. Now, start a conversation with the first sentence!
            """.trimIndent() + textMessage

            listOf(MultiChatMessage("system", """
            You are helpful assistant supporting people learning $language.
            Your name is RolePlayingBot.
            Please assume that the user you are assisting is a $level in $language.
            And please write only the sentence without the character role.

            Let's have a conversation in $language. Please answer in $language only
            without providing a translation. And please don't write down the pronunciation either.
            Let us assume that the situation in $scenarioEng.
            I am $myRoleEng. The character I want you to act as is $gptRoleEng.
            Please make sure that I'm a $level in $language, so please use $level words
            as much as possible. Now, start a conversation with the first sentence!
            """.trimIndent()),
                MultiChatMessage("user",textMessage))

        }else{
            listOf(MultiChatMessage("system", """
            You are helpful assistant supporting people learning $language.
            Your name is RolePlayingBot.
            Please assume that the user you are assisting is a $level in $language.
            And please write only the sentence without the character role.

            Let's have a conversation in $language. Please answer in $language only
            without providing a translation. And please don't write down the pronunciation either.
            Let us assume that the situation in $scenarioEng.
            I am $myRoleEng. The character I want you to act as is $gptRoleEng.
            Please make sure that I'm a $level in $language, so please use $level words
            as much as possible. Respond to user messages, assuming that there may be messages you have previously communicated with.
            """.trimIndent()),
                MultiChatMessage("user",textMessage))
        }

        // 받은(me) 메세지 저장
        chatMessageService.createChatMessage(
            ChatMessageRequestDto(
                message = textMessage,
                sender = "me",
                chatRoomId = roomId,
            )
        )

            // Chat GPT API로부터 응답을 받아옵니다.
            val response = chatGptService.generateResponse(prompt)

//            val response = "test chat message!!!"
            // gpt 메세지 저장
            if(!response.isNullOrBlank()){
                chatMessageService.createChatMessage(
                    ChatMessageRequestDto(
                        message = response,
                        sender = "gpt",
                        chatRoomId = roomId,
                    )
                )
            }

            // 클라이언트에게 응답을 전송합니다.
            session.sendMessage(TextMessage(response!!))

    }

    fun extractJsonPayload(input: String): String {
        val startIndex = input.indexOf('{')
        return if (startIndex != -1) {
            val endIndex = input.lastIndexOf('}')
            if (endIndex != -1) {
                input.substring(startIndex, endIndex + 1)
            } else {
                input.substring(startIndex)
            }
        } else {
            input
        }
    }

}