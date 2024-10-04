package com.jellyone.lab1.controller

import com.jellyone.lab1.dto.AdminRequestDto
import com.jellyone.lab1.dto.SignInRequest
import com.jellyone.lab1.dto.SignupRequest
import com.jellyone.lab1.service.AuthService
import com.jellyone.lab1.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api")
@Tag(name = "User Management")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/signup")
    @Operation(summary = "Sign up user", description = "Sign up user")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "User successfully signed up",
            content = [
                Content(
                    schema = Schema(implementation = String::class)
                )
            ]
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun signup(@RequestBody request: SignupRequest): ResponseEntity<String> {
        return ResponseEntity.ok(userService.registerUser(request.username, request.password))
    }

    @PostMapping("/admin-requests/submit")
    @Operation(summary = "Submit admin request", description = "Submit admin request")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Admin request successfully submitted",
            content = [
                Content(
                    schema = Schema(implementation = String::class)
                )
            ],
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun submitAdminRequest(@RequestBody request: AdminRequestDto): ResponseEntity<String> {
        return ResponseEntity.ok(userService.requestAdmin(request.username, request.password))
    }

    @PostMapping("/admin-requests/approve/{id}")
    @Operation(summary = "Approve admin request", description = "Approve admin request")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Admin request successfully approved",
            content = [
                Content(
                    schema = Schema(implementation = String::class)
                )
            ],
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun approveAdminRequest(@PathVariable id: Long): ResponseEntity<String> {
        userService.approveAdminRequest(id)
        return ResponseEntity.ok("Admin request approved")
    }

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current user")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Current user successfully retrieved",
            content = [
                Content(
                    schema = Schema(implementation = String::class)
                )
            ]
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun me(principal: Principal): ResponseEntity<String> {
        logger.info("request on /me. casting to string: ${principal.name}")
        return ResponseEntity.ok(principal.name)
    }
}