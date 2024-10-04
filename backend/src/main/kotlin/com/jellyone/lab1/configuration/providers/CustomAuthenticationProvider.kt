package com.jellyone.lab1.configuration.providers

import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.service.UserService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class CustomAuthenticationProvider(
    private val userService: UserService
) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        if (!userService.checkPassword(username, password)) {
            throw UsernameNotFoundException("Username or password is incorrect")
        }

        val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(Role.USER.toString()))

        return  UsernamePasswordAuthenticationToken(username, password, authorities)
//        return UsernamePasswordAuthenticationToken(username, password, authorities).apply {
//            isAuthenticated = true
//        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
