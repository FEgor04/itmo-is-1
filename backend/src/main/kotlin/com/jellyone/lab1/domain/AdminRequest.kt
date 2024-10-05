package com.jellyone.lab1.domain

import com.jellyone.lab1.domain.enums.AdminRequestStatus

data class AdminRequest(
    val id: Long,
    val username: String,
    val password: String,
    var status: AdminRequestStatus
)