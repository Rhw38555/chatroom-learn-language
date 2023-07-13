package com.example.chatlanguage.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        // 권한에 따라 허용하는 url 설정
        // /login, /signup 페이지는 모두 허용, 다른 페이지는 인증된 사용자만 허용
        http.csrf()
            .ignoringAntMatchers("/h2-console/**")
            .disable()
            .authorizeRequests()
            // 페이지 권한 설정
            .antMatchers("/list","/createPage","/chatPage","/chat/**").authenticated()
            .antMatchers("/signUpPage", "/login", "/signup", "/h2-console/**").anonymous()
            .anyRequest().permitAll();

        http.headers().frameOptions().disable()


        // login 설정
        http
            .formLogin()
            .loginPage("/loginPage") // GET 요청 (login form을 보여줌)
            .loginProcessingUrl("/login") // POST 요청 (login 창에 입력한 데이터를 처리)
            .usernameParameter("username") // login에 필요한 id 값을 email로 설정 (default는 username)
            .passwordParameter("password") // login에 필요한 password 값을 password(default)로 설정
            .defaultSuccessUrl("/list") // login에 성공하면 /로 redirect
            .permitAll()

        // logout 설정
        http
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/loginPage") // logout에 성공하면 /로 redirect
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        // 정적 리소스들이 보안필터를 거치지 않게끔
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/font/**")
        }
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}