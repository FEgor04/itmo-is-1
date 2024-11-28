package com.jellyone.lab1.controller

import com.jellyone.lab1.service.ImportService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/import")
@Tag(name = "Import Management")
@SecurityRequirement(name = "JWT")
class ImportController(
    private val importService: ImportService
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
        @RequestParam("file") file: MultipartFile
    ) {
        importService.import(file.inputStream)
    }
}