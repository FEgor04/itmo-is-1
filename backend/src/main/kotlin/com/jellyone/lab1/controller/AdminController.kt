package com.jellyone.lab1.controller

import com.jellyone.lab1.mapper.RequestAdminMapper
import com.jellyone.lab1.repository.AdminRequestRepository
import com.jellyone.lab1.repository.map
import com.jellyone.lab1.service.impl.UserServiceImpl
import com.jellyone.lab1.web.security.principal.IAuthenticationFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/admin/requests")
@Tag(name = "Admin Management")
@SecurityRequirement(name = "JWT")
class AdminController(
    private val userService: UserServiceImpl,
    private val adminRequestAdminMapper: RequestAdminMapper
) {

    @PostMapping("/submit")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve/{username}")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rejected/{username}")
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

    @GetMapping("")
    @Operation(summary = "Get all admin requests", description = "Returns all admin requests")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "All admin requests successfully retrieved",
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getAllAdminRequests(
        page: Int,
        pageSize: Int,
        @Schema(allowableValues = ["username", "status"])
        sortBy: String,
        @Schema(allowableValues = ["asc", "desc"])
        sortDirection: String,
        username: String?,
    ) = userService.getAllAdminRequests(
        page,
        pageSize,
        AdminRequestRepository.AdminRequestFields.entries.find { it.dbName == sortBy }!!,
        sortDirection == "asc",
        username
    ).map { adminRequestAdminMapper.toDto(it) }

}