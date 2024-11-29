package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.Car
import io.swagger.v3.oas.annotations.media.Schema
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CarRepository(private val dsl: DSLContext) {

    @Transactional
    fun findById(id: Long): Car? {
        return dsl.selectFrom("car")
            .where(DSL.field("id").eq(id))
            .fetchOneInto(Car::class.java)
    }

    @Transactional
    fun findAll(
        page: Int,
        pageSize: Int,
        sortBy: CarFields,
        sortAsc: Boolean,
        modelFilter: String?,
        brandFilter: String?,
        color: String?,
    ): PaginatedResponse<Car> {
        val dslWhere =
            DSL.field("lower(model)")
                .contains(modelFilter?.lowercase() ?: "")
                .and(
                    DSL.field("lower(brand)")
                        .contains(brandFilter?.lowercase() ?: "")
                )
                .and(DSL.field("lower(color)").contains(color?.lowercase() ?: ""))
        val total = dsl.fetchCount(
            dsl.selectFrom("car")
                .where(
                    dslWhere
                )
        )
        val values = dsl.selectFrom("car")
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
            .fetchInto(Car::class.java)
        return PaginatedResponse(
            page,
            pageSize,
            total,
            values,
        )
    }

    enum class CarFields(val dbName: String) {
        ID("id"),
        BRAND("brand"),
        MODEL("model"),
        COOL("cool"),
        COLOR("color"),
    }

    @Transactional
    fun save(car: Car): Car {
        val record = dsl.insertInto(DSL.table("car"))
            .set(DSL.field("color"), car.color)
            .set(DSL.field("model"), car.model)
            .set(DSL.field("brand"), car.brand)
            .set(DSL.field("cool"), car.cool)
            .set(DSL.field("owner_id"), car.ownerId)
            .returning(DSL.field("id"))
            .fetchOne()

        val id = record?.get(DSL.field("id")) as Long?

        return car.copy(id = id)
    }

    @Transactional
    fun saveAll(cars: List<Car>): List<Car> {
        val generatedIds = mutableListOf<Long>()

        cars.forEach { car ->
            val result = dsl.insertInto(DSL.table("car"))
                .set(DSL.field("color"), car.color)
                .set(DSL.field("model"), car.model)
                .set(DSL.field("brand"), car.brand)
                .set(DSL.field("cool"), car.cool)
                .set(DSL.field("owner_id"), car.ownerId)
                .returning(DSL.field("id"))
                .fetchOne()

            result?.let {
                generatedIds.add(it[DSL.field("id", Long::class.java)] as Long)
            }
        }

        return cars.mapIndexed { index, car ->
            car.copy(id = generatedIds[index])
        }
    }

    @Transactional
    fun update(car: Car): Car? {
        if (findById(car.id ?: return null) == null) return null

        dsl.update(DSL.table("car"))
            .set(DSL.field("color"), car.color)
            .set(DSL.field("model"), car.model)
            .set(DSL.field("brand"), car.brand)
            .set(DSL.field("cool"), car.cool)
            .set(DSL.field("owner_id"), car.ownerId)
            .where(DSL.field("id").eq(car.id))
            .execute()

        return car
    }

    @Transactional
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
