package com.jellyone.lab1.controller

import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.dto.AdminRequestDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management")
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
    fun submitAdminRequest(@RequestBody request: AdminRequestDto): ResponseEntity<String> {
        return ResponseEntity.ok(userService.requestAdmin(request.username, request.password))
    }

    @PostMapping("/requests/approve/{id}")
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
}