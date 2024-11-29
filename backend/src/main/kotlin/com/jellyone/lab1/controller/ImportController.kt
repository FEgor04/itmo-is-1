package com.jellyone.lab1.controller

import com.jellyone.lab1.repository.PaginatedResponse
import com.jellyone.lab1.repository.map
import com.jellyone.lab1.service.ImportService
import com.jellyone.lab1.web.dto.ImportDto
import com.jellyone.lab1.web.dto.toDto
import com.jellyone.lab1.web.security.principal.IAuthenticationFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart

@RestController
@RequestMapping("/api/import")
@Tag(name = "Import Management")
@SecurityRequirement(name = "JWT")
class ImportController(
    private val importService: ImportService,
    private val authenticationFacade: IAuthenticationFacade
) {

    @PostMapping("", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "Import file",
        description = "Allows you to upload a file for processing",
        responses = [
            ApiResponse(
                description = "Successful import",
                responseCode = "200",
                content = [Content(schema = Schema(implementation = ImportDto::class))]
            )
        ],
        requestBody = RequestBody(
            description = "File for import",
            required = true,
            content = [
                Content(
                    mediaType = "multipart/form-data",
                    schema = Schema(type = "object", format = "binary")
                )
            ]
        )
    )
    fun import(
        @RequestPart("file") file: MultipartFile,
        principal: Principal
    ): ImportDto {
        val import = importService.createProgressImport(principal.name)
        return importService.import(file.inputStream, import, principal.name).toDto()
    }

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