package com.jellyone.lab1.domain.logs

import java.time.LocalDateTime

class HumanBeingsLogs(
    val id: Long,
    val humanId: Long,
    val action: LogAction,
    val username: String,
    val date: LocalDateTime
) {
}