package com.example.chatlanguage.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class ChatGptClient(
    private val restTemplate: RestTemplate,
    @param:Value("\${chat.gpt.api.url}") private val apiUrl: String
) {
    fun generateResponse(message: String): String? {
        // Chat GPT API에 요청을 보내고 응답을 받는 로직을 구현합니다.
        // 요청에 필요한 데이터를 구성하고 API 호출을 수행합니다.
        // 실제로는 적절한 HTTP 요청을 생성하고 응답을 처리해야 합니다.
        // 아래는 예시로 RestTemplate을 사용한 GET 요청을 보내는 방식입니다.

        // API 응답을 가공 또는 필요에 따라 추가 처리합니다.
        // (예: 응답 데이터 파싱, 특정 필드 추출 등)
        return restTemplate.getForObject("$apiUrl?message=$message", String::class.java)
    }
}