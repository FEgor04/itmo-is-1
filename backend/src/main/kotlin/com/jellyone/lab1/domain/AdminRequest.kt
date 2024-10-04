package com.jellyone.lab1.domain

data class AdminRequest(
    val id: Long,
    val username: String,
    val password: String,
    var status: String
)