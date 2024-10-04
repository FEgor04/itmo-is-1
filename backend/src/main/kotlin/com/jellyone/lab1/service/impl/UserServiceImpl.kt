package com.jellyone.lab1.service.impl

import com.jellyone.lab1.domain.AdminRequest
import com.jellyone.lab1.domain.User
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.repository.AdminRequestRepository
import com.jellyone.lab1.repository.UserRepository
import com.jellyone.lab1.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val adminRequestRepository: AdminRequestRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun registerUser(username: String, password: String) {
        if (userRepository.findByUsername(username) != null) {
            throw RuntimeException("User already exists")
        }

        val user = User(
            id = 0,
            username = username,
            password = passwordEncoder.encode(password),
            role = Role.USER
        )
        userRepository.save(user)
    }

    override fun requestAdmin(username: String, password: String): String {
        return if (userRepository.countByRole(Role.ADMIN.name) > 0) {
            val request = AdminRequest(
                id = 0,
                username = username,
                password = passwordEncoder.encode(password),
                status = "PENDING"
            )
            adminRequestRepository.save(request)
            "Admin request submitted"
        } else {
            val user = User(
                id = 0,
                username = username,
                password = passwordEncoder.encode(password),
                role = Role.USER
            )
            userRepository.save(user)
            "Admin registered successfully"
        }
    }

    override fun approveAdminRequest(id: Long) {
        val request = adminRequestRepository.findById(id)
        if (request.status == "PENDING") {
            val user = User(
                id = 0,
                username = request.username,
                password = request.password,
                role = Role.USER
            )
            userRepository.save(user)

            request.status = "APPROVED"
            adminRequestRepository.update(request)
        }
    }

    override fun getByUserId(id: Long) = userRepository.findById(id)

    override fun getByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw RuntimeException("User not found")
    }

    override fun checkPassword(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username)
            ?: throw RuntimeException("User not found")

        return user.password == passwordEncoder.encode(password)
    }

}