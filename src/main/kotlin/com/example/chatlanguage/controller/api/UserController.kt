package com.example.chatlanguage.controller.api

import com.example.chatlanguage.domain.dto.UserCreateRequestDto
import com.example.chatlanguage.domain.dto.UserCreateResponseDto
import com.example.chatlanguage.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController @Autowired constructor(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody userCreateRequestDto: UserCreateRequestDto): ResponseEntity<UserCreateResponseDto> {
        val createdUser = userService.createUser(userCreateRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

}