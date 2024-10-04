package com.jellyone.lab1.service

import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return User(user.username, user.password, getAuthorities(user.role))
    }

    private fun getAuthorities(role: Role): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(role.name))
    }
}