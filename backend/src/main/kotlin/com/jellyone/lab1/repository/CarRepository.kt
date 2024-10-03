package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.Car
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class CarRepository(private val dsl: DSLContext) {

    fun findById(id: Long): Car? {
        return dsl.selectFrom("car")
            .where(DSL.field("id").eq(id))
            .fetchOneInto(Car::class.java)
    }

    fun findAll(
        page: Int,
        pageSize: Int,
        modelFilter: String?,
        brandFilter: String?,
    ): PaginatedResponse<Car> {
        val values = dsl.selectFrom("car")
            .where(DSL.field("model").contains(modelFilter ?: "").and(DSL.field("brand").contains(brandFilter ?: "")))
            .limit(pageSize)
            .offset((page - 1) * pageSize)
            .fetchInto(Car::class.java)
        return PaginatedResponse(
            page,
            pageSize,
            50,
            values,
        )
    }

    fun save(car: Car): Car {
        val record = dsl.insertInto(DSL.table("car"))
            .set(DSL.field("color"), car.color)
            .set(DSL.field("model"), car.model)
            .set(DSL.field("brand"), car.brand)
            .set(DSL.field("cool"), car.cool)
            .returning(DSL.field("id"))
            .fetchOne()

        val id = record?.get(DSL.field("id")) as Long?

        return car.copy(id = id)
    }

    fun update(car: Car): Car? {
        if (findById(car.id ?: return null) == null) return null

        dsl.update(DSL.table("car"))
            .set(DSL.field("color"), car.color)
            .set(DSL.field("model"), car.model)
            .set(DSL.field("brand"), car.brand)
            .set(DSL.field("cool"), car.cool)
            .where(DSL.field("id").eq(car.id))
            .execute()

        return car
    }

    fun deleteById(id: Long): Boolean {
        // Сначала находим всех human_being, связанных с данной машиной
        val humanBeings = dsl.selectFrom("human_being")
            .where(DSL.field("car_id").eq(id))
            .fetch()

        // Удаляем всех human_being
        for (record in humanBeings) {
            val humanId = record.get("id") as Long
            dsl.deleteFrom(DSL.table("human_being"))
                .where(DSL.field("id").eq(humanId))
                .execute()
        }

        // Теперь удаляем саму машину
        val rowsDeleted = dsl.deleteFrom(DSL.table("car"))
            .where(DSL.field("id").eq(id))
            .execute()

        return rowsDeleted > 0
    }
}
