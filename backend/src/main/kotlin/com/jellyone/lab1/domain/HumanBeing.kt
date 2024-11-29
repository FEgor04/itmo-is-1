package com.jellyone.lab1.domain

import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import java.time.LocalDate

data class HumanBeing(
    val id: Long? = null,
    val name: String,
    val coordinates: Coordinates,
    val creationDate: LocalDate = LocalDate.now(),
    val realHero: Boolean,
    val hasToothpick: Boolean,
    var car: Car? = null,
    val mood: Mood? = null,
    val impactSpeed: Long? = null,
    val weaponType: WeaponType,
    val ownerId: Long
)