package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.Import
import com.jellyone.lab1.service.UserService
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ImportRepository(
    private val dsl: DSLContext,
    private val userService: UserService
) {
    @Transactional
    fun getAll(page: Int, pageSize: Int): PaginatedResponse<Import> {
        val total = dsl.fetchCount(dsl.selectFrom("import"))
        val values = dsl.selectFrom("import")
            .orderBy(DSL.field("created_at").desc())
            .limit(pageSize)
            .offset((page - 1) * pageSize)
            .fetchInto(Import::class.java)
        return PaginatedResponse(page, pageSize, total, values)
    }

    @Transactional
    fun getAllByUser(page: Int, pageSize: Int, username: String): PaginatedResponse<Import> {
        val user = userService.getByUsername(username)
        val total = dsl.fetchCount(dsl.selectFrom("import"))
        val values = dsl.selectFrom("import")
            .where(DSL.field("user_id").eq(user.id))
            .orderBy(DSL.field("created_at").desc())
            .limit(pageSize)
            .offset((page - 1) * pageSize)
            .fetchInto(Import::class.java)
        return PaginatedResponse(page, pageSize, total, values)
    }

    @Transactional
    fun create(import: Import) {
        dsl.insertInto(DSL.table("import"))
            .set(DSL.field("status"), import.status)
            .set(DSL.field("message"), import.message)
            .set(DSL.field("created_entities_count"), import.createdEntitiesCount)
            .set(DSL.field("created_at"), import.createdAt)
            .set(DSL.field("finished_at"), import.finishedAt)
            .set(DSL.field("user_id"), import.user.id)
            .execute()
    }

    fun update(import: Import) {
        dsl.update(DSL.table("import"))
            .set(DSL.field("status"), import.status)
            .set(DSL.field("message"), import.message)
            .set(DSL.field("created_entities_count"), import.createdEntitiesCount)
            .set(DSL.field("created_at"), import.createdAt)
            .set(DSL.field("finished_at"), import.finishedAt)
            .set(DSL.field("user_id"), import.user.id)
            .where(DSL.field("id").eq(import.id))
            .execute()
    }
}