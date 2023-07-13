package com.example.chatlanguage.service

import com.example.chatlanguage.client.ChatGptClient
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage
import io.github.flashvayne.chatgpt.service.ChatgptService
import org.springframework.stereotype.Service

@Service
class MyChatGptService(private val chatGptService: ChatgptService) {


    fun generateResponse(message: List<MultiChatMessage>?): String? {

        //ChatGptService를 사용하여 Chat GPT API에 요청을 보냅니다.
        // Chat GPT에서 받은 응답을 가공 또는 필요에 따라 추가 처리합니다.
        return message?.let {
            chatGptService.multiChat(message)
        }
    }
    fun generateResponseBasic(message: String): String? {

        //ChatGptService를 사용하여 Chat GPT API에 요청을 보냅니다.
        // Chat GPT에서 받은 응답을 가공 또는 필요에 따라 추가 처리합니다.
        return message?.let {
            chatGptService.sendMessage(message)
        }
    }
}