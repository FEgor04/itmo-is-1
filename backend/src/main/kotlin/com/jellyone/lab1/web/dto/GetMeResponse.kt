package com.jellyone.lab1.web.dto

import com.jellyone.lab1.domain.enums.AdminRequestStatus
import com.jellyone.lab1.domain.enums.Role
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO for read operations on Car")
data class GetMeResponse(
    @Schema(description = "The username of the user", example = "John Doe")
    val username: String,

    @Schema(description = "The ID of the user", example = "1")
    val id: Long,

    @Schema(description = "The role of the user", example = "ADMIN")
    val role: Role,

    @Schema(description = "The status of the admin request", example = "PENDING")
    val adminRequestStatus: AdminRequestStatus
) {
}