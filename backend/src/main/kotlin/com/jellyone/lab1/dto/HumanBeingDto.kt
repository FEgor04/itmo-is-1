package com.jellyone.lab1.dto

import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import java.time.LocalDate

data class HumanBeingDto(
    val id: Long?,
    val name: String,
    val x: Double,
    val y: Double,
    val creationDate: LocalDate,
    val realHero: Boolean,
    val hasToothpick: Boolean,
    val carId: Long,
    val mood: Mood?,
    val impactSpeed: Long,
    val weaponType: WeaponType
)