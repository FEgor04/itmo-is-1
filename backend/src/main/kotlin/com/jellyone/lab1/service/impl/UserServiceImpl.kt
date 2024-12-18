package com.jellyone.lab1.service.impl

import com.jellyone.lab1.domain.AdminRequest
import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.User
import com.jellyone.lab1.domain.enums.AdminRequestStatus
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.exception.ResourceAlreadyExistsException
import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.repository.AdminRequestRepository
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.UserRepository
import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.security.principal.IAuthenticationFacade
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val adminRequestRepository: AdminRequestRepository,
    private val passwordEncoder: PasswordEncoder,
    private val autenticationFacade: IAuthenticationFacade
) : UserService {

    @Transactional
    override fun registerUser(username: String, password: String) {
        if (userRepository.findByUsername(username) != null) {
            throw ResourceAlreadyExistsException("User already exists")
        }

        val user = User(
            id = 0,
            username = username,
            password = passwordEncoder.encode(password),
            role = Role.USER
        )
        userRepository.save(user)
    }

    @Transactional
    override fun requestAdmin(username: String, password: String): String {
        return if (userRepository.countByRole(Role.ADMIN.name) > 0) {
            val request = AdminRequest(
                id = 0,
                username = username,
                password = password,
                status = AdminRequestStatus.valueOf("PENDING")
            )
            adminRequestRepository.save(request)
            return "Admin request submitted"
        } else {
            val user = User(
                id = 0,
                username = username,
                password = password,
                role = Role.ADMIN
            )
            autenticationFacade.setAdminRole()
            userRepository.update(user)
            return "Admin registered successfully"
        }
    }

    @Transactional
    override fun approveAdminRequest(username: String) {
        val request = adminRequestRepository.findAdminRequestByUsername(username)
        if (request.status == AdminRequestStatus.valueOf("PENDING")) {
            val user = User(
                id = 0,
                username = request.username,
                password = request.password,
                role = Role.ADMIN
            )
            userRepository.update(user)

            request.status = AdminRequestStatus.valueOf("APPROVED")
            adminRequestRepository.update(request)
        }
    }

    @Transactional
    override fun rejecteAdminRequest(username: String) {
        val request = adminRequestRepository.findAdminRequestByUsername(username)
        if (request.status == AdminRequestStatus.valueOf("PENDING")) {

            request.status = AdminRequestStatus.valueOf("REJECTED")
            adminRequestRepository.update(request)
        }
    }

    @Transactional
    fun getAllAdminRequests(
        page: Int,
        pageSize: Int,
        sortBy: AdminRequestRepository.AdminRequestFields,
        sortAsc: Boolean,
        username: String?,
    ) = adminRequestRepository.findAll(page, pageSize, sortBy, sortAsc, username)

    @Transactional
    override fun getAdminRequestStatus(username: String): AdminRequestStatus {
        return adminRequestRepository.findAdminRequestStatusByUsername(username);
    }

    @Transactional
    override fun getUserIdByUsername(username: String): Long {
        val user = userRepository.findByUsername(username)
        return user?.id ?: throw ResourceNotFoundException("User not found")
    }

    @Transactional
    override fun getByUserId(id: Long) = userRepository.findById(id)

    @Transactional
    override fun getByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw ResourceNotFoundException("User not found")
    }

    @Transactional
    override fun checkPassword(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username)
            ?: throw ResourceNotFoundException("User not found")

        return user.password == passwordEncoder.encode(password)
    }

}