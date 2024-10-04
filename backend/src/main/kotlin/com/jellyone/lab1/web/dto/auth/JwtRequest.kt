package com.jellyone.lab1.web.dto.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT Request", accessMode = Schema.AccessMode.READ_ONLY)
data class JwtRequest(

    @field:Schema(description = "Username")
    @field:NotNull(message = "Username must not be null")
    var username: String,

    @field:Schema(description = "Password")
    @field:NotNull(message = "Password must not be null")
    var password: String
)