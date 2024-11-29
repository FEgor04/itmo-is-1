package com.jellyone.lab1.web.dto

import com.jellyone.lab1.domain.Import
import com.jellyone.lab1.domain.enums.ImportStatus
import java.time.LocalDateTime

data class ImportDto(
    val id: Long,
    val status: ImportStatus,
    val message: String?,
    val createdEntitiesCount: Long,
    val createdAt: LocalDateTime,
    val finishedAt: LocalDateTime?,
    val author: UserDto
)

fun Import.toDto() = ImportDto(
    id!!,
    status,
    message,
    createdEntitiesCount,
    createdAt,
    finishedAt,
    user.toDto()
)