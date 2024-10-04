package com.jellyone.lab1.domain

import com.jellyone.lab1.domain.enums.Role

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val role: Role
)