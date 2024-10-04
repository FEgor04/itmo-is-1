package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.User
import com.jellyone.lab1.domain.enums.Role
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class UserRepository(private val dsl: DSLContext) {

    fun findByUsername(username: String): User? {
        val result = dsl.select(DSL.field("id"), DSL.field("username"), DSL.field("password"), DSL.field("role"))
            .from(DSL.table("users"))
            .where(DSL.field("username").eq(username))
            .fetchOne()

        return result?.let {
            User(
                it.value1() as Long,
                it.value2() as String,
                it.value3() as String,
                Role.valueOf(it.value4() as String)
            )
        }
    }

    fun findById(id: Long): User? {
        return dsl.selectFrom("users")
            .where(DSL.field("id").eq(id))
            .fetchOneInto(User::class.java)
    }

    fun countByRole(role: String): Int {
        return dsl.selectCount()
            .from(DSL.table("users"))
            .where(DSL.field("role").eq(role))
            .fetchOne(0, Int::class.java) ?: 0
    }

    fun save(user: User) {
        dsl.insertInto(DSL.table("users"))
            .set(DSL.field("username"), user.username)
            .set(DSL.field("password"), user.password)
            .set(DSL.field("role"), user.role.name)
            .execute()
    }
}
