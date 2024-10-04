package com.jellyone.lab1.service.impl

import com.jellyone.lab1.domain.User
import com.jellyone.lab1.service.AuthService
import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.security.JwtTokenProvider
import com.jellyone.lab1.web.dto.auth.JwtRequest
import com.jellyone.lab1.web.dto.auth.JwtResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager

@Service
class AuthServiceImpl @Autowired constructor(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthService {

    override fun login(loginRequest: JwtRequest): JwtResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
        } catch (e: Exception) {
            throw BadCredentialsException(e.message)
        }

        val user: User = userService.getByUsername(loginRequest.username)

        return JwtResponse(
            user.id,
            user.username,
            jwtTokenProvider.createAccessToken(user.id, user.username, user.role),
            jwtTokenProvider.createRefreshToken(user.id, user.username)
        )
    }

    override fun refresh(refreshToken: String): JwtResponse {
        return jwtTokenProvider.refreshUserTokens(refreshToken)
    }
}
