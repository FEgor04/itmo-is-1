package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.Import
import com.jellyone.lab1.domain.User
import com.jellyone.lab1.domain.enums.ImportStatus
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.service.UserService
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.time.LocalDateTime

@Repository
class ImportRepository(
    private val dsl: DSLContext,
    private val userService: UserService
) {
    @Transactional
    fun getAll(page: Int, pageSize: Int): PaginatedResponse<Import> {
        val total = dsl.fetchCount(dsl.selectFrom("import"))

        val result = dsl.select(
            DSL.field("import.id"),
            DSL.field("import.status"),
            DSL.field("import.message"),
            DSL.field("import.created_entities_count"),
            DSL.field("import.created_at"),
            DSL.field("import.finished_at"),
            DSL.field("users.id"),
            DSL.field("users.username"),
            DSL.field("users.role"),
            DSL.field("users.password")
        )
            .from("import")
            .join("users").on(DSL.field("import.user_id").eq(DSL.field("users.id")))
            .orderBy(DSL.field("import.created_at").desc())
            .limit(pageSize)
            .offset((page - 1) * pageSize)
            .fetch()

        val importsWithUsers = result.map { record ->
            val import = Import(
                id = record.get(DSL.field("import.id"), Long::class.java),
                status = ImportStatus.valueOf(record.get(DSL.field("import.status"), String::class.java)),
                message = record.get(DSL.field("import.message"), String::class.java),
                createdEntitiesCount = record.get(DSL.field("import.created_entities_count"), Long::class.java),
                createdAt = record.get(DSL.field("import.created_at"), java.sql.Timestamp::class.java)
                    .toLocalDateTime(),
                finishedAt = record.get(DSL.field("import.finished_at"), java.sql.Timestamp::class.java)
                    .toLocalDateTime(),
                user = User(
                    id = record.get(DSL.field("users.id"), Long::class.java),
                    username = record.get(DSL.field("users.username"), String::class.java),
                    role = Role.valueOf(record.get(DSL.field("users.role"), String::class.java)),
                    password = record.get(DSL.field("users.password"), String::class.java)
                )
            )
            import
        }

        return PaginatedResponse(page, pageSize, total, importsWithUsers)
    }

    @Transactional
    fun getAllByUser(page: Int, pageSize: Int, username: String): PaginatedResponse<Import> {
        val user = userService.getByUsername(username)

        val total = dsl.fetchCount(
            dsl.selectFrom("import")
                .where(DSL.field("import.user_id").eq(user.id))
        )

        val result = dsl.select(
            DSL.field("import.id"),
            DSL.field("import.status"),
            DSL.field("import.message"),
            DSL.field("import.created_entities_count"),
            DSL.field("import.created_at"),
            DSL.field("import.finished_at"),
            DSL.field("users.id"),
            DSL.field("users.username"),
            DSL.field("users.role"),
            DSL.field("users.password")
        )
            .from("import")
            .join("users").on(DSL.field("import.user_id").eq(DSL.field("users.id")))
            .where(DSL.field("import.user_id").eq(user.id))
            .orderBy(DSL.field("import.created_at").desc())
            .limit(pageSize)
            .offset((page - 1) * pageSize)
            .fetch()

        val importsWithUsers = result.map { record ->
            val import = Import(
                id = record.get(DSL.field("import.id"), Long::class.java),
                status = ImportStatus.valueOf(record.get(DSL.field("import.status"), String::class.java)),
                message = record.get(DSL.field("import.message"), String::class.java),
                createdEntitiesCount = record.get(DSL.field("import.created_entities_count"), Long::class.java),
                createdAt = record.get(DSL.field("import.created_at"), java.sql.Timestamp::class.java)
                    .toLocalDateTime(),
                finishedAt = record.get(DSL.field("import.finished_at"), java.sql.Timestamp::class.java)
                    .toLocalDateTime(),
                user = User(
                    id = record.get(DSL.field("users.id"), Long::class.java),
                    username = record.get(DSL.field("users.username"), String::class.java),
                    role = Role.valueOf(record.get(DSL.field("users.role"), String::class.java)),
                    password = record.get(DSL.field("users.password"), String::class.java)
                )
            )
            import
        }

        return PaginatedResponse(page, pageSize, total, importsWithUsers)
    }


    @Transactional
    fun create(import: Import): Import {
        val result = dsl.insertInto(DSL.table("import"))
            .set(DSL.field("status"), import.status.name)
            .set(DSL.field("message"), import.message)
            .set(DSL.field("created_entities_count"), import.createdEntitiesCount)
            .set(DSL.field("created_at"), import.createdAt)
            .set(DSL.field("finished_at"), import.finishedAt)
            .set(DSL.field("user_id"), import.user.id)
            .returning(DSL.field("id"))
            .fetchOne()

        return import.copy(id = result?.get(DSL.field("id", Long::class.java)))
    }

    fun update(import: Import) {
        dsl.update(DSL.table("import"))
            .set(DSL.field("status"), import.status.name)
            .set(DSL.field("message"), import.message)
            .set(DSL.field("created_entities_count"), import.createdEntitiesCount)
            .set(DSL.field("created_at"), import.createdAt)
            .set(DSL.field("finished_at"), import.finishedAt)
            .set(DSL.field("user_id"), import.user.id)
            .where(DSL.field("id").eq(import.id))
            .execute()
    }

    private fun getLocalDateFromSqlDate(date: java.sql.Date): LocalDateTime {
        return date.toLocalDate().atStartOfDay()
    }
}