package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.AdminRequest
import com.jellyone.lab1.domain.enums.AdminRequestStatus
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class AdminRequestRepository(private val dsl: DSLContext) {

    @Transactional
    fun save(request: AdminRequest) {
        dsl.insertInto(DSL.table("admin_requests"))
            .set(DSL.field("username"), request.username)
            .set(DSL.field("password"), request.password)
            .set(DSL.field("status"), request.status.toString())
            .execute()
    }

    @Transactional
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
                AdminRequestStatus.valueOf(it.value4() as String)
            )
        } ?: throw NoSuchElementException("Request with ID $id not found.")
    }

    @Transactional
    fun findAll(
        page: Int,
        pageSize: Int,
        sortBy: AdminRequestFields,
        sortAsc: Boolean,
        username: String?,
    ): PaginatedResponse<AdminRequest> {
        val dslWhere =
            DSL.field("lower(username)")
                .contains(username?.lowercase() ?: "")
        val total = dsl.fetchCount(
            dsl.selectFrom("admin_requests")
                .where(
                    dslWhere
                )
        )
        val values = dsl.selectFrom("admin_requests")
            .where(
                dslWhere
            )
            .orderBy(DSL.field(sortBy.dbName).let {
                if (sortAsc) {
                    it.asc()
                } else {
                    it.desc()
                }
            })
            .limit(pageSize)
            .offset((page - 1) * pageSize)
            .fetchInto(AdminRequest::class.java)
        return PaginatedResponse(
            page,
            pageSize,
            total,
            values,
        )
    }

    @Transactional
    fun update(request: AdminRequest) {
        val rowsUpdated = dsl.update(DSL.table("admin_requests"))
            .set(DSL.field("username"), request.username)
            .set(DSL.field("password"), request.password)
            .set(DSL.field("status"), request.status.toString())
            .where(DSL.field("id").eq(request.id))
            .execute()

        if (rowsUpdated == 0) {
            throw NoSuchElementException("Request with ID ${request.id} not found for update.")
        }
    }

    @Transactional
    fun findAdminRequestStatusByUsername(username: String): AdminRequestStatus {
        val result = dsl.select(DSL.field("id"), DSL.field("username"), DSL.field("password"), DSL.field("status"))
            .from(DSL.table("admin_requests"))
            .where(DSL.field("username").eq(username))
            .fetchOne()

        return result?.let {
            AdminRequestStatus.valueOf(it.value4() as String)
        } ?: AdminRequestStatus.NO_REQUEST
    }

    @Transactional
    fun findAdminRequestByUsername(username: String): AdminRequest {
        val result = dsl.select(DSL.field("id"), DSL.field("username"), DSL.field("password"), DSL.field("status"))
            .from(DSL.table("admin_requests"))
            .where(DSL.field("username").eq(username))
            .fetchOne()

        return result?.let {
            AdminRequest(
                it.value1() as Long,
                it.value2() as String,
                it.value3() as String,
                AdminRequestStatus.valueOf(it.value4() as String)
            )
        } ?: throw NoSuchElementException("Request with username $username not found.")
    }

    enum class AdminRequestFields(val dbName: String) {
        ID("id"),
        USERNAME("username"),
        PASSWORD("password"),
        STATUS("status"),
    }
}