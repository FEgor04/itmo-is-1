package com.jellyone.lab1.domain.logs

import java.time.LocalDateTime

data class CarsLogs(
    val id: Long,
    val carId: Long,
    val action: LogAction,
    val username: String,
    val date: LocalDateTime
) {}