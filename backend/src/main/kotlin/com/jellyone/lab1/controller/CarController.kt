package com.jellyone.lab1.controller

import com.jellyone.lab1.mapper.CarMapper
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.map
import com.jellyone.lab1.service.CarService
import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.dto.CarDTO
import com.jellyone.lab1.web.dto.CreateCarDTO
import com.jellyone.lab1.web.dto.UpdateCarDTO
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

@RestController
@RequestMapping("/api/cars")
@Tag(name = "Car Management")
@SecurityRequirement(name = "JWT")
class CarController(private val carService: CarService, private val userService: UserService) {

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved all humans being"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("")
    fun getAllCars(
        page: Int,
        pageSize: Int,
        @Schema(allowableValues = ["id", "brand", "model", "cool", "color"])
        sortBy: String,
        @Schema(allowableValues = ["asc", "desc"])
        sortDirection: String,
        model: String?,
        brand: String?,
        color: String?
    ) = carService.getAllCars(
        page,
        pageSize,
        CarRepository.CarFields.entries.find { it.dbName == sortBy }!!,
        sortDirection == "asc",
        model,
        brand,
        color,
    ).map { CarMapper.toDto(it) }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID", description = "Returns a car by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved car",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CarDTO::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "Car not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getCarById(@PathVariable id: Long): ResponseEntity<CarDTO> {
        val car = carService.getCarById(id)
        return if (car != null) {
            ResponseEntity.ok(CarMapper.toDto(car))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "Create a new car", description = "Creates a new car and returns its details")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Car successfully created",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CarDTO::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createCar(@RequestBody carDto: CreateCarDTO, principal: Principal): ResponseEntity<CarDTO> {

        val createdCar = carService.createCar(
            CarService.CreateCarRequest(
                model = carDto.model,
                brand = carDto.brand,
                color = carDto.color,
                cool = carDto.cool,
                ownerId = userService.getUserIdByUsername(principal.name)
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(CarMapper.toDto(createdCar))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update car information", description = "Updates an existing car's information")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Car successfully updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CarDTO::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "Car not found"),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateCar(@PathVariable id: Long, @RequestBody carDto: UpdateCarDTO, principal: Principal): ResponseEntity<CarDTO> {
        val updatedCar = carService.updateCar(id, CarMapper.toEntityFromUpdate(carDto), principal.name)
        return if (updatedCar != null) {
            ResponseEntity.ok(CarMapper.toDto(updatedCar))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car", description = "Deletes a car by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Car successfully deleted"),
            ApiResponse(responseCode = "404", description = "Car not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deleteCar(@PathVariable id: Long, principal: Principal): ResponseEntity<Void> {
        return if (carService.deleteCar(id, principal.name)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
