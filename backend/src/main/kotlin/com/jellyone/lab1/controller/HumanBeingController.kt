package com.jellyone.lab1.controller

import com.jellyone.lab1.dto.HumanBeingDto
import com.jellyone.lab1.service.HumanBeingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/humans")
@Tag(name = "Human Management")
class HumanBeingController(private val humanBeingService: HumanBeingService) {

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved all humans being"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("")
    fun getAllHumans(): List<HumanBeingDto> {
        return humanBeingService.getAllHumans()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a human by ID", description = "Returns a human by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved human",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = HumanBeingDto::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "Human not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getHumanById(@PathVariable id: Long): ResponseEntity<HumanBeingDto> {
        val humanDto = humanBeingService.getHumanById(id)
        return if (humanDto != null) {
            ResponseEntity.ok(humanDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "Create a new human", description = "Creates a new human and returns its details")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Human successfully created",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = HumanBeingDto::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createHuman(@RequestBody humanBeingDto: HumanBeingDto): ResponseEntity<HumanBeingDto> {
        val createdHuman = humanBeingService.createHuman(humanBeingDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHuman)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update human information", description = "Updates an existing human's information")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Human successfully updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = HumanBeingDto::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "Human not found"),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateHuman(@PathVariable id: Long, @RequestBody humanBeingDto: HumanBeingDto): ResponseEntity<HumanBeingDto> {
        val updatedHuman = humanBeingService.updateHuman(id, humanBeingDto)
        return if (updatedHuman != null) {
            ResponseEntity.ok(updatedHuman)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a human", description = "Deletes a human by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Human successfully deleted"),
            ApiResponse(responseCode = "404", description = "Human not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deleteHuman(@PathVariable id: Long): ResponseEntity<Void> {
        return if (humanBeingService.deleteHuman(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
