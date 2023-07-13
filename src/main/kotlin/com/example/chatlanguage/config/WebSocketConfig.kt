package com.example.chatlanguage.config

import com.example.chatlanguage.handler.ChatWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.web.client.RestTemplate
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketConfig(private val chatWebSocketHandler: ChatWebSocketHandler) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(chatWebSocketHandler, "/chatLanguageGpt")
            .setAllowedOrigins("http://localhost:8080")
            .withSockJS()
            .setClientLibraryUrl("https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js")
    }

    @Bean
    fun restTemplate() : RestTemplate{
        return RestTemplate()
    }
    @Bean
    fun securityContextRepository() : SecurityContextRepository {
        return HttpSessionSecurityContextRepository()
    }

}

