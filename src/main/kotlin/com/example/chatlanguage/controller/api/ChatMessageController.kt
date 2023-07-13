package com.example.chatlanguage.controller.api

import com.example.chatlanguage.domain.dto.*
import com.example.chatlanguage.service.ChatMessageService
import com.example.chatlanguage.service.ChatRoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class ChatMessageController @Autowired constructor(
    private val chatMessageService: ChatMessageService,
) {

    @GetMapping("/chatMessages/{roomId}")
    fun getChatMessageByChatRoomId(@PathVariable roomId: String) : ResponseEntity<ChatMessageListResponseDto>{
        return ResponseEntity.status(HttpStatus.OK)
            .body(chatMessageService.findAllChatMessage(roomId))
    }

    @PostMapping("/chatMessages")
    fun getChatRoomsByUserId(@RequestBody chatMessageRequestDto : ChatMessageRequestDto) : ResponseEntity<ChatMessageResponseDto> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(chatMessageService.createChatMessage(chatMessageRequestDto))
    }

    @PostMapping("/commentRecommend")
    fun commentRecommend(@RequestBody commentRecommendRequestDto: CommentRecommendRequestDto) : ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(chatMessageService.commentRecommend(commentRecommendRequestDto))
    }

}