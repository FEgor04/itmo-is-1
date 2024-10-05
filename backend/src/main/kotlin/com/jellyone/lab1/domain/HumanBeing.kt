package com.jellyone.lab1.domain

import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import jakarta.persistence.*
import java.time.LocalDate

@Table(name = "human_being")
@Entity
data class HumanBeing(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Embedded
    val coordinates: Coordinates,

    @Column(name = "creation_date", nullable = false, updatable = false)
    val creationDate: LocalDate = LocalDate.now(),

    @Column(name = "real_hero")
    val realHero: Boolean,

    @Column(name = "has_toothpick")
    val hasToothpick: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    val car: Car,

    @Enumerated(EnumType.STRING)
    @Column(name = "mood")
    val mood: Mood? = null,

    @Column(name = "impact_speed", nullable = false)
    val impactSpeed: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "weapon_type", nullable = false)
    val weaponType: WeaponType
)