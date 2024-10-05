package com.jellyone.lab1.service

import com.jellyone.lab1.domain.User
import com.jellyone.lab1.domain.enums.AdminRequestStatus

interface UserService {
    fun registerUser(username: String, password: String)

    fun requestAdmin(username: String, password: String): String

    fun approveAdminRequest(username: String)

    fun rejecteAdminRequest(username: String)

    fun getByUserId(id: Long): User?

    fun getByUsername(username: String): User

    fun checkPassword(username: String, password: String): Boolean

    fun getAdminRequestStatus(username: String): AdminRequestStatus

    fun getUserIdByUsername(username: String): Long
}