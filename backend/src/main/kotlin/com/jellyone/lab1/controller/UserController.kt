package com.jellyone.lab1.controller

import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.dto.GetMeResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api")
@Tag(name = "User Management")
@SecurityRequirement(name = "JWT")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current user")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Current user successfully retrieved",
            content = [
                Content(
                    schema = Schema(implementation = GetMeResponse::class)
                )
            ]
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun me(principal: Principal): GetMeResponse {
        val user = userService.getByUsername(principal.name)
        val adminRequestStatus = userService.getAdminRequestStatus(principal.name)
        return GetMeResponse(principal.name, user.id, user.role, adminRequestStatus)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/amIAdmin")
    @Operation(summary = "Get current admin-user", description = "Get current admin-user")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Current admin-user successfully retrieved",
            content = [
                Content(
                    schema = Schema(implementation = GetMeResponse::class)
                )
            ]
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun checkAdmin(principal: Principal): GetMeResponse {
        val user = userService.getByUsername(principal.name)
        val adminRequestStatus = userService.getAdminRequestStatus(principal.name)
        return GetMeResponse(principal.name, user.id, user.role, adminRequestStatus)
    }

}