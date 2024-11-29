package com.jellyone.lab1.domain

import com.jellyone.lab1.domain.enums.ImportStatus
import java.time.LocalDateTime

data class Import(
    val id: Long? = 0,
    var status: ImportStatus,
    var message: String?,
    var createdEntitiesCount: Long,
    val createdAt: LocalDateTime,
    var finishedAt: LocalDateTime?,
    val user: User,
)