package com.jellyone.lab1.mapper

import com.jellyone.lab1.domain.AdminRequest
import com.jellyone.lab1.web.dto.AdminRequestDto
import org.springframework.stereotype.Component

@Component
class RequestAdminMapper {
    fun toDto(request: AdminRequest): AdminRequestDto {
        return AdminRequestDto(request.username, request.status)
    }
}