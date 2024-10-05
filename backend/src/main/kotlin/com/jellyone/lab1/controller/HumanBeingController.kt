package com.jellyone.lab1.controller

import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.web.dto.CreateHumanBeingDto
import com.jellyone.lab1.web.dto.HumanBeingDto
import com.jellyone.lab1.mapper.HumanBeingMapper
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.repository.map
import com.jellyone.lab1.service.CarService
import com.jellyone.lab1.service.HumanBeingService
import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.dto.PutHumanBeingDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDate

@RestController
@RequestMapping("api/humans")
@Tag(name = "Human Management")
@SecurityRequirement(name = "JWT")
class HumanBeingController(
    private val humanBeingService: HumanBeingService,
    private val carService: CarService,
    private val humanBeingMapper: HumanBeingMapper,
    private val userService: UserService
) {

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved all humans being"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("")
    fun getAllHumans(
        page: Int,
        pageSize: Int,
        @Schema(
            allowableValues = ["id", "name", "creationDate", "realHero", "hasToothpick",
                "mood", "impactSpeed", "weaponType","coordinates.x","coordinates.y",
            "carId","car.brand","car.model","car.cool","car.color"]
        )
        sortBy: String,
        @Schema(allowableValues = ["asc", "desc"])
        sortDirection: String,
        name: String?,
        impactSpeedLT: Double?
    ) = humanBeingService.getAllHumans(
        page,
        pageSize,
        HumanBeingRepository.HumanBeingFields.entries.find { it.entityName == sortBy }!!,
        sortDirection == "asc",
        name,
        impactSpeedLT
    ).map { humanBeingMapper.toDto(it, carService.getCarById(it.car.id)!!) }


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
        val humanBeing =
            humanBeingService.getHumanById(id) ?: throw ResourceNotFoundException("HumanBeing not found with id $id")
        val car =
            carService.getCarById(humanBeing.car.id) ?: throw ResourceNotFoundException("Car not found with id $id")
        val updatedHuman = humanBeingMapper.toDto(humanBeing, car);
        return ResponseEntity.ok(updatedHuman)
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
    fun createHuman(
        @RequestBody humanBeingDto: CreateHumanBeingDto,
        principal: Principal
    ): ResponseEntity<HumanBeingDto> {
        val createdHuman = humanBeingService.createHuman(
            HumanBeingService.CreateHumanBeingRequest(
                name = humanBeingDto.name,
                x = humanBeingDto.x,
                y = humanBeingDto.y,
                creationDate = LocalDate.now(),
                realHero = humanBeingDto.realHero,
                hasToothpick = humanBeingDto.hasToothpick,
                carId = humanBeingDto.carId,
                mood = humanBeingDto.mood,
                impactSpeed = humanBeingDto.impactSpeed,
                weaponType = humanBeingDto.weaponType,
                ownerId = userService.getUserIdByUsername(principal.name)
            )
        )
        val car = carService.getCarById(humanBeingDto.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeingDto.carId}")
        return ResponseEntity.status(HttpStatus.CREATED).body(humanBeingMapper.toDto(createdHuman, car))
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
    fun updateHuman(
        @PathVariable id: Long,
        @RequestBody humanBeingDto: PutHumanBeingDto,
        principal: Principal
    ): ResponseEntity<HumanBeingDto> {

        val humanBeing = humanBeingService.updateHuman(id, humanBeingDto, principal.name)
            ?: throw ResourceNotFoundException("humanBeing not found with id $id")
        val car =
            carService.getCarById(humanBeing.car.id) ?: throw ResourceNotFoundException("Car not found with id $id")
        val updatedHuman = humanBeingMapper.toDto(humanBeing, car);
        return ResponseEntity.ok(updatedHuman)
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
    fun deleteHuman(@PathVariable id: Long, principal: Principal): ResponseEntity<Void> {
        return if (humanBeingService.deleteHuman(id, principal.name)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
