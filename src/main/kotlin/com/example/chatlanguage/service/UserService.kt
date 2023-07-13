package com.example.chatlanguage.service

import com.example.chatlanguage.domain.dto.UserCreateRequestDto
import com.example.chatlanguage.domain.dto.UserCreateResponseDto
import com.example.chatlanguage.domain.dto.UserLoginRequestDto
import com.example.chatlanguage.domain.entity.User
import com.example.chatlanguage.domain.repository.UserRepository
import com.example.userservice.exception.NotFoundUserException
import com.example.userservice.exception.PasswordNotMatchedException
import com.example.userservice.exception.UserExistException
import com.example.userservice.exception.UsernameNotMatchedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter


@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) : UserDetailsService {
    fun createUser(userCreateDto: UserCreateRequestDto): UserCreateResponseDto {

        userValidation(userCreateDto.username, userCreateDto.password)

        if(userRepository.findByUsername(userCreateDto.username) != null){
            throw UserExistException()
        }

        val user = User(
            username = userCreateDto.username,
            password = passwordEncoder.encode(userCreateDto.password),
        )

        return userRepository.save(user).let {
            UserCreateResponseDto(username = it.username, createdAt = it.createdAt!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        }
    }

    fun authenticate(userDTO: UserLoginRequestDto): Boolean {
        userValidation(userDTO.username, userDTO.password)
        
        val user = userRepository.findByUsername(userDTO.username) ?: throw NotFoundUserException()

        if (!passwordEncoder.matches(userDTO.password, user!!.password)) {
            throw PasswordNotMatchedException()
        }
        
        return true
    }
    
    override fun loadUserByUsername(username: String?): UserDetails {
        userIdValidation(username)

        val user = userRepository.findByUsername(username!!) ?: throw NotFoundUserException()

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(SimpleGrantedAuthority("USER"))
            .build();

    }

    private fun userIdValidation(username: String?){
        if(username.isNullOrBlank()){
            throw UsernameNotMatchedException()
        }
    }

    private fun userValidation(username: String, password: String){
        if(username.isNullOrBlank()){
            throw UsernameNotMatchedException()
        }
        if(password.isNullOrBlank()){
            throw PasswordNotMatchedException()
        }
    }
}
