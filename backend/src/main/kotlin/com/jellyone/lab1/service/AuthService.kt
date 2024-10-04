package com.jellyone.lab1.service

import com.jellyone.lab1.dto.SignInRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val userService: UserService
) {

    fun signIn(signInRequest: SignInRequest) {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            signInRequest.username,
            signInRequest.password
        )

        val authentication: Authentication = authenticationManager.authenticate(authenticationToken)

        SecurityContextHolder.getContext().authentication = authentication
        return;
    }
}
