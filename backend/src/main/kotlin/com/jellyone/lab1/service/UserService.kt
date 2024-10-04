package com.jellyone.lab1.service

import com.jellyone.lab1.domain.User

interface UserService {
    fun registerUser(username: String, password: String)

    fun requestAdmin(username: String, password: String): String

    fun approveAdminRequest(id: Long)

    fun getByUserId(id: Long): User?

    fun getByUsername(username: String): User

    fun checkPassword(username: String, password: String): Boolean
}