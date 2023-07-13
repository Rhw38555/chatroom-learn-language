package com.example.chatlanguage.controller.api

import com.example.chatlanguage.domain.dto.ChatRoomCreateRequestDto
import com.example.chatlanguage.domain.dto.ChatRoomListResponseDto
import com.example.chatlanguage.domain.dto.ChatRoomResponseDto
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
class ChatRoomController @Autowired constructor(
    private val chatRoomService: ChatRoomService,
) {

    @GetMapping("/chatRooms/{userId}")
    fun getChatRoomsByUserId(@PathVariable userId: String) : ResponseEntity<ChatRoomListResponseDto>{
        return ResponseEntity.status(HttpStatus.OK)
            .body(chatRoomService.findAllChatRoom(userId))
    }

    @PostMapping("/chatRooms")
    fun createChatRoom(@RequestBody chatRoomCreateRequestDto: ChatRoomCreateRequestDto, principal: Principal) : ResponseEntity<ChatRoomResponseDto>{
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(chatRoomService.createRoom(chatRoomCreateRequestDto, principal.name))
    }

}