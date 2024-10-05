package com.jellyone.lab1.web.dto

import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "DTO for read operations on Car")
data class CarDTO(
    @Schema(description = "The unique identifier of the car", example = "1", nullable = true)
    val id: Long,

    @Schema(description = "The color of the car", example = "Red")
    val color: String,

    @Schema(description = "The model of the car", example = "Civic")
    val model: String,

    @Schema(description = "The brand of the car", example = "Honda")
    val brand: String,

    @Schema(description = "Indicates if the car is cool", example = "true")
    val cool: Boolean,

    @Schema(description = "The owner of the car", example = "1")
    val ownerId: Long
)

@Schema(description = "DTO for read operations on Car")
data class CreateCarDTO(
    @Schema(description = "The color of the car", example = "Red")
    val color: String,

    @Schema(description = "The model of the car", example = "Civic")
    val model: String,

    @Schema(description = "The brand of the car", example = "Honda")
    val brand: String,

    @Schema(description = "Indicates if the car is cool", example = "true")
    val cool: Boolean
)

@Schema(description = "DTO for read operations on Car")
data class UpdateCarDTO(
    @Schema(description = "The color of the car", example = "Red")
    val color: String,

    @Schema(description = "The model of the car", example = "Civic")
    val model: String,

    @Schema(description = "The brand of the car", example = "Honda")
    val brand: String,

    @Schema(description = "Indicates if the car is cool", example = "true")
    val cool: Boolean,
)