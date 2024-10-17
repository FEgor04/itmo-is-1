package com.jellyone.lab1.domain

import com.jellyone.lab1.domain.enums.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: Long,
    private val username: String,
    private val password: String,
    val role: Role
) : UserDetails {


    override fun getAuthorities(): Collection<SimpleGrantedAuthority> {
        val list = mutableListOf<SimpleGrantedAuthority>()
        list.add(SimpleGrantedAuthority(role.toString()))
        return list
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return false
    }

}