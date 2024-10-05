package com.jellyone.lab1.controller

import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.dto.AdminRequestDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management")
@SecurityRequirement(name = "JWT")
class AdminController(
    private val userService: UserService,
) {

    @PostMapping("/requests/submit")
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
    fun submitAdminRequest(principal: Principal): ResponseEntity<String> {
        val user = userService.getByUsername(principal.name)
        return ResponseEntity.ok(userService.requestAdmin(user.username, user.password))
    }

    @PostMapping("/requests/approve/{username}")
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
    fun approveAdminRequest(@PathVariable username: String): ResponseEntity<String> {
        userService.approveAdminRequest(username)
        return ResponseEntity.ok("Admin request approved")
    }

    @PostMapping("/requests/rejected/{username}")
    @Operation(summary = "Rejected admin request", description = "Rejected admin request")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Admin request successfully rejected",
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
    fun rejectedAdminRequest(@PathVariable username: String): ResponseEntity<String> {
        userService.rejecteAdminRequest(username)
        return ResponseEntity.ok("Admin request rejected")
    }
}