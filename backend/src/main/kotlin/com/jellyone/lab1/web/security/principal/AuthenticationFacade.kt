package com.jellyone.lab1.web.security.principal

import com.jellyone.lab1.domain.enums.Role
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
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

    override fun setAdminRole() {
        val currentAuth = getAuthentication()

        val newAuthorities = mutableListOf<GrantedAuthority>().apply {
            addAll(currentAuth.authorities)
            add(SimpleGrantedAuthority("ROLE_ADMIN"))
        }

        val newAuth = UsernamePasswordAuthenticationToken(
            currentAuth.principal,
            currentAuth.credentials,
            newAuthorities
        )

        SecurityContextHolder.getContext().authentication = newAuth
    }

    override fun isAdmin(): Boolean {
        return getAuthentication().authorities?.contains(SimpleGrantedAuthority(Role.ADMIN.name.uppercase()))
            ?: false
    }
}