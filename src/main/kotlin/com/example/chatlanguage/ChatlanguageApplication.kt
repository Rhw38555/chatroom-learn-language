package com.example.chatlanguage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class ChatlanguageApplication

fun main(args: Array<String>) {
    runApplication<ChatlanguageApplication>(*args)
}
