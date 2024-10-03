package com.jellyone.lab1.repository

data class PaginatedResponse<T>(
    val page: Int,
    val pageSize: Int,
    val total: Int,
    val values: List<T>,
)

fun <T, V> PaginatedResponse<T>.map(mapper: (T) -> V): PaginatedResponse<V> {
    return PaginatedResponse(
        page,
        pageSize,
        total,
        values.map(mapper)
    )
}