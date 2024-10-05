package com.jellyone.lab1.domain

data class Car(
    val id: Long? = null,
    val color: String = "",
    val model: String = "",
    val brand: String = "",
    val cool: Boolean = false,
    val ownerId: Long
)