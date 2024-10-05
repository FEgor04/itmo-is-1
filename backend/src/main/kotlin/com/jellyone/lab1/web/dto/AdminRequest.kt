package com.jellyone.lab1.web.dto

import com.jellyone.lab1.domain.enums.AdminRequestStatus

data class AdminRequestDto(val username: String, val status: AdminRequestStatus)