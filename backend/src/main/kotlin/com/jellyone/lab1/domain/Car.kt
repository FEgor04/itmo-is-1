package com.jellyone.lab1.domain

import jakarta.persistence.*

@Table(name = "car")
@Entity
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long?=null,

    @Column(name = "color", nullable = false)
    val color: String = "",

    @Column(name = "model", nullable = false)
    val model: String = "",

    @Column(name = "brand", nullable = false)
    val brand: String = "",

    @Column(name = "cool", nullable = false)
    val cool: Boolean = false
)