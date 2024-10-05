package com.jellyone.lab1.web.dto

import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "Data Transfer Object for a Human Being")
data class HumanBeingDto(
    @Schema(description = "The unique identifier of the human being", example = "1", nullable = true)
    val id: Long?,

    @Schema(description = "The name of the human being", example = "John Doe")
    val name: String,

    @Schema(description = "The X coordinate of the human being's location", example = "12.34")
    val x: Double,

    @Schema(description = "The Y coordinate of the human being's location", example = "56.78")
    val y: Double,

    @Schema(description = "The date of creation of the human being record", example = "2023-10-01")
    val creationDate: LocalDate,

    @Schema(description = "Indicates if the human being is a real hero", example = "true")
    val realHero: Boolean,

    @Schema(description = "Indicates if the human being has a toothpick", example = "false")
    val hasToothpick: Boolean,

    @Schema(description = "The ID of the car associated with the human being", example = "100")
    val car: CarDTO,

    @Schema(description = "The mood of the human being", example = "APATHY")
    val mood: Mood?,

    @Schema(description = "The impact speed of the human being", example = "150")
    val impactSpeed: Long?,

    @Schema(description = "The type of weapon the human being has", example = "PISTOL")
    val weaponType: WeaponType,

    @Schema(description = "The owner of the car", example = "1")
    val ownerId: Long
)

data class CreateHumanBeingDto(
    @Schema(description = "The name of the human being", example = "John Doe")
    val name: String,

    @Schema(description = "The X coordinate of the human being's location", example = "12.34")
    val x: Double,

    @Schema(description = "The Y coordinate of the human being's location", example = "56.78")
    val y: Double,

    @Schema(description = "Indicates if the human being is a real hero", example = "true")
    val realHero: Boolean,

    @Schema(description = "Indicates if the human being has a toothpick", example = "false")
    val hasToothpick: Boolean,

    @Schema(description = "The ID of the car associated with the human being", example = "100")
    val carId: Long,

    @Schema(description = "The mood of the human being", example = "APATHY")
    val mood: Mood?,

    @Schema(description = "The impact speed of the human being", example = "150")
    val impactSpeed: Long?,

    @Schema(description = "The type of weapon the human being has", example = "PISTOL")
    val weaponType: WeaponType
) {}


data class PutHumanBeingDto(
    @Schema(description = "The unique identifier of the human being", example = "1", nullable = true)
    val id: Long?,

    @Schema(description = "The name of the human being", example = "John Doe")
    val name: String,

    @Schema(description = "The X coordinate of the human being's location", example = "12.34")
    val x: Double,

    @Schema(description = "The Y coordinate of the human being's location", example = "56.78")
    val y: Double,

    @Schema(description = "Indicates if the human being is a real hero", example = "true")
    val realHero: Boolean,

    @Schema(description = "Indicates if the human being has a toothpick", example = "false")
    val hasToothpick: Boolean,

    @Schema(description = "The ID of the car associated with the human being", example = "100")
    val carId: Long,

    @Schema(description = "The mood of the human being", example = "APATHY")
    val mood: Mood?,

    @Schema(description = "The impact speed of the human being", example = "150")
    val impactSpeed: Long?,

    @Schema(description = "The type of weapon the human being has", example = "PISTOL")
    val weaponType: WeaponType
) {}
