package com.jellyone.lab1.web.dto

import com.jellyone.lab1.domain.User

data class UserDto(
    val id: Long,
    val username: String,
)

fun User.toDto() = UserDto(id, username)