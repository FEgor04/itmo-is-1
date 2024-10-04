package com.jellyone.lab1.service

import com.jellyone.lab1.domain.AdminRequest
import com.jellyone.lab1.domain.User
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.repository.AdminRequestRepository
import com.jellyone.lab1.repository.UserRepository
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val adminRequestRepository: AdminRequestRepository,
    private val shacoder: SHACoder
) {

    fun registerUser(username: String, password: String): String {
        if (userRepository.findByUsername(username) != null) {
            throw RuntimeException("User already exists")
        }

        val hashedPassword = hashPassword(password)

        val user = User(id = 0, username = username, password = hashedPassword, role = Role.USER)
        userRepository.save(user)

        return "User registered successfully"
    }

    fun requestAdmin(username: String, password: String): String {
        return if (userRepository.countByRole(Role.ADMIN.name) > 0) {
            val hashedPassword = hashPassword(password)
            val request = AdminRequest(id = 0, username = username, password = hashedPassword, status = "PENDING")
            adminRequestRepository.save(request)
            "Admin request submitted"
        } else {
            val hashedPassword = hashPassword(password)
            val user = User(id = 0, username = username, password = hashedPassword, role = Role.ADMIN)
            userRepository.save(user)
            "Admin registered successfully"
        }
    }

    fun approveAdminRequest(id: Long) {
        val request = adminRequestRepository.findById(id)
        if (request.status == "PENDING") {
            val user =
                User(id = 0, username = request.username, password = request.password, role = Role.ADMIN)
            userRepository.save(user)

            request.status = "APPROVED"
            adminRequestRepository.update(request)
        }
    }

    fun checkPassword(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username)
            ?: throw RuntimeException("User not found")

        val hashedPassword = hashPassword(password)
        return user.password == hashedPassword
    }

    private fun hashPassword(password: String): String {
        return shacoder.getSHA512SecurePassword(password, "salt");
    }
}