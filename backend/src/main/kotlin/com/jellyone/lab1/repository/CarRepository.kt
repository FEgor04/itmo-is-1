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
}