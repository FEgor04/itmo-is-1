package com.jellyone.lab1.web.dto.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import lombok.Data

@Schema(description = "JWT Response")
@Data
data class JwtResponse(

    @field:Schema(description = "User ID")
    @field:NotNull
    val id: Long,

    @field:Schema(description = "Username")
    @field:NotNull
    val username: String,

    @field:Schema(description = "Access token")
    @field:NotNull
    val accessToken: String,

    @field:Schema(description = "Refresh token")
    @field:NotNull
    val refreshToken: String
)