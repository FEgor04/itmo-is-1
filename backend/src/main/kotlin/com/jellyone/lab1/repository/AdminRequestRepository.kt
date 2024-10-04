package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.AdminRequest
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class AdminRequestRepository(private val dsl: DSLContext) {

    fun save(request: AdminRequest) {
        dsl.insertInto(DSL.table("admin_requests"))
            .set(DSL.field("username"), request.username)
            .set(DSL.field("password"), request.password)
            .set(DSL.field("status"), request.status)
            .execute()
    }


    fun findById(id: Long): AdminRequest {
        val result = dsl.select(DSL.field("id"), DSL.field("username"), DSL.field("password"), DSL.field("status"))
            .from(DSL.table("admin_requests"))
            .where(DSL.field("id").eq(id))
            .fetchOne()

        return result?.let {
            AdminRequest(
                it.value1() as Long,
                it.value2() as String,
                it.value3() as String,
                it.value4() as String
            )
        } ?: throw NoSuchElementException("Request with ID $id not found.")
    }

    fun update(request: AdminRequest) {
        val rowsUpdated = dsl.update(DSL.table("admin_requests"))
            .set(DSL.field("username"), request.username)
            .set(DSL.field("password"), request.password)
            .set(DSL.field("status"), request.status)
            .where(DSL.field("id").eq(request.id))
            .execute()

        if (rowsUpdated == 0) {
            throw NoSuchElementException("Request with ID ${request.id} not found for update.")
        }
    }
}
