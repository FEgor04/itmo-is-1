package com.jellyone.lab1.web.security.principal

import org.springframework.security.core.Authentication

interface IAuthenticationFacade {
    fun getAuthentication(): Authentication
    fun getAuthName(): String
}