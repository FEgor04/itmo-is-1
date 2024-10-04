package com.jellyone.lab1.web.security.expression

import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.web.security.JwtEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service("customSecurityExpression")
class CustomSecurityExpression() {

    fun canAccessUser(id: Long): Boolean {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val user: JwtEntity = authentication.principal as JwtEntity
        val userId: Long = user.id

        return userId == id || hasAnyRole(authentication, Role.ADMIN)
    }

    private fun hasAnyRole(authentication: Authentication, vararg roles: Role): Boolean {
        for (role in roles) {
            val authority = SimpleGrantedAuthority(role.name)
            if (authentication.authorities.contains(authority)) {
                return true
            }
        }
        return false
    }

    fun hasAdminRole(): Boolean {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return hasAnyRole(authentication, Role.ADMIN)
    }
}