package com.jellyone.lab1.service

import com.jellyone.lab1.web.dto.auth.JwtRequest
import com.jellyone.lab1.web.dto.auth.JwtResponse

interface AuthService {
    fun login(loginRequest: JwtRequest): JwtResponse
    fun refresh(refreshToken: String): JwtResponse
}
