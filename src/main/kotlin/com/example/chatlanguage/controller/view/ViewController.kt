package com.example.chatlanguage.controller.view

import com.example.chatlanguage.enum.LanguageType
import com.example.chatlanguage.enum.LevelType
import com.example.chatlanguage.service.ChatMessageService
import com.example.chatlanguage.service.ChatRoomService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
class ViewController(
    private val chatRoomService: ChatRoomService,
    private val chatMessageService: ChatMessageService,
) {

    @RequestMapping("/list")
    fun listView(model: Model, principal: Principal) : String{
        model.addAttribute("chatList", chatRoomService.findAllChatRoom(principal.name))
        return "list"
    }

    @RequestMapping("/createPage")
    fun createView(model: Model) : String{
        model.addAttribute("languageOptions", LanguageType.values().map { it.value })
        model.addAttribute("levelOptions", LevelType.values().map { it.value })
        return "create"
    }

    @RequestMapping("/chat/{id}")
    fun detailView(@PathVariable id:String, model: Model) : String{
        // roomId 넘김
        val chatRoomResponseDto = chatRoomService.findById(id)
        model.addAttribute("room", chatRoomResponseDto)
        val chatMessageListResponseDto = chatMessageService.findAllChatMessage(id)
        model.addAttribute("messages", chatMessageListResponseDto)
        return "chat"
    }

    @RequestMapping("/chatPage")
    fun chatView() : String{
        return "chat"
    }

    @RequestMapping("/loginPage")
    fun loginView() : String{
        return "login"
    }

    @RequestMapping("/signUpPage")
    fun signUpView() : String{
        return "signup"
    }


}