package com.example.chatlanguage.controller.api

import com.example.chatlanguage.domain.dto.TranslateDTO
import com.example.chatlanguage.service.ApiTranslateNmt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TranslateController @Autowired constructor(
    private val apiTranslateNmt: ApiTranslateNmt,
) {

    @PostMapping("/translate")
    fun getChatRoomsByUserId(@RequestBody translateDTO: TranslateDTO) : ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(apiTranslateNmt.getTransSentence(translateDTO.koreanText))
    }

}