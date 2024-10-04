package com.jellyone.lab1.web.security.principal

import com.jellyone.lab1.domain.enums.Role
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationFacade : IAuthenticationFacade {

    override fun getAuthentication(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }

    override fun getAuthName(): String {
        return getAuthentication().name ?: "Unknown"
    }

    fun isAdmin(): Boolean {
        return getAuthentication().authorities?.contains(SimpleGrantedAuthority("ROLE_${Role.ADMIN.name.uppercase()}"))
            ?: false
    }
}