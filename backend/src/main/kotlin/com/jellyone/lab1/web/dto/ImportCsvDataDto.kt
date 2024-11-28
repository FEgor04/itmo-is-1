package com.jellyone.lab1.web.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ImportCsvDataDto(
    @JsonProperty("name")
    val name: String,

    @JsonProperty("x")
    val x: Double,

    @JsonProperty("y")
    val y: Double,

    @JsonProperty("realHero")
    val realHero: Boolean,

    @JsonProperty("hasToothpick")
    val hasToothpick: Boolean,

    @JsonProperty("mood")
    val mood: String?,

    @JsonProperty("speed")
    val speed: Long?,

    @JsonProperty("weaponType")
    val weaponType: String,

    @JsonProperty("car.model")
    val carModel: String,

    @JsonProperty("car.brand")
    val carBrand: String,

    @JsonProperty("car.color")
    val carColor: String,

    @JsonProperty("car.cool")
    val carCool: Boolean,

    @JsonProperty("ownerId")
    val ownerId: Long
)