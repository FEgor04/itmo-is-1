package com.jellyone.lab1.mapper

import com.jellyone.lab1.domain.AdminRequest
import com.jellyone.lab1.dto.AdminRequestDto

class RequestAdminMapper {
    fun toDto(request: AdminRequest): AdminRequestDto {
        return AdminRequestDto(request.username, request.password)
    }
}