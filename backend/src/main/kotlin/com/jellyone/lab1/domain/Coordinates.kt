package com.jellyone.lab1.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity

@Embeddable
data class Coordinates(
    @Column(name = "x", nullable = false)
    val x: Double = 0.0,

    @Column(name = "y", nullable = false)
    val y: Double = 0.0
)