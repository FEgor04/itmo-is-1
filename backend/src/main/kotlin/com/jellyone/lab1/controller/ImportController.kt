package com.jellyone.lab1.controller

import com.jellyone.lab1.repository.PaginatedResponse
import com.jellyone.lab1.repository.map
import com.jellyone.lab1.service.ImportService
import com.jellyone.lab1.web.dto.ImportDto
import com.jellyone.lab1.web.dto.toDto
import com.jellyone.lab1.web.security.principal.IAuthenticationFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/api/import")
@Tag(name = "Import Management")
@SecurityRequirement(name = "JWT")
class ImportController(
    private val importService: ImportService,
    private val authenticationFacade: IAuthenticationFacade
) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Import started"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @Operation(summary = "Import", description = "Imports data from csv file")
    @PostMapping
    fun import(
        @RequestParam("file") file: MultipartFile,
        principal: Principal
    ) = importService.import(file.inputStream, importService.createProgressImport(principal.name)).toDto()

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved all imports"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @Operation(summary = "Get all imports", description = "Returns all imports")
    @GetMapping("")
    fun getAllImports(
        page: Int,
        pageSize: Int
    ): PaginatedResponse<ImportDto> {
        return if (authenticationFacade.isAdmin()) {
            importService.getAll(page, pageSize).map { import ->
                import.toDto()
            }
        } else {
            importService.getAllByUser(page, pageSize, authenticationFacade.getAuthName()).map { import ->
                import.toDto()
            }
        }
    }
}