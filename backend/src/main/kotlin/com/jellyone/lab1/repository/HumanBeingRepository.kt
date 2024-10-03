package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.HumanBeing
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class HumanBeingRepository(private val dsl: DSLContext) {

    fun findById(id: Long): HumanBeing? {
        return dsl.selectFrom("human_being")
            .where(DSL.field("id").eq(id))
            .fetchOneInto(HumanBeing::class.java)
    }

    fun findAll(): List<HumanBeing> {
        return dsl.selectFrom("human_being")
            .fetchInto(HumanBeing::class.java)
    }

    fun save(humanBeing: HumanBeing): HumanBeing {
        val record = dsl.insertInto(DSL.table("human_being"))
            .set(DSL.field("name"), humanBeing.name)
            .set(DSL.field("x"), humanBeing.coordinates.x)
            .set(DSL.field("y"), humanBeing.coordinates.y)
            .set(DSL.field("creation_date"), humanBeing.creationDate)
            .set(DSL.field("real_hero"), humanBeing.realHero)
            .set(DSL.field("has_toothpick"), humanBeing.hasToothpick)
            .set(DSL.field("car_id"), humanBeing.car?.id)
            .set(DSL.field("mood"), humanBeing.mood?.name)
            .set(DSL.field("impact_speed"), humanBeing.impactSpeed)
            .set(DSL.field("weapon_type"), humanBeing.weaponType.name)
            .returning(DSL.field("id"))
            .fetchOne()

        val id = record?.get(DSL.field("id")) as Long?

        return humanBeing.copy(id = id)
    }

    fun update(humanBeing: HumanBeing): HumanBeing? {
        if (findById(humanBeing.id ?: return null) == null) return null

        dsl.update(DSL.table("human_being"))
            .set(DSL.field("name"), humanBeing.name)
            .set(DSL.field("x"), humanBeing.coordinates.x)
            .set(DSL.field("y"), humanBeing.coordinates.y)
            .set(DSL.field("creation_date"), humanBeing.creationDate)
            .set(DSL.field("real_hero"), humanBeing.realHero)
            .set(DSL.field("has_toothpick"), humanBeing.hasToothpick)
            .set(DSL.field("car_id"), humanBeing.car.id)
            .set(DSL.field("mood"), humanBeing.mood?.name)
            .set(DSL.field("impact_speed"), humanBeing.impactSpeed)
            .set(DSL.field("weapon_type"), humanBeing.weaponType.name)
            .where(DSL.field("id").eq(humanBeing.id))
            .execute()

        return humanBeing
    }

    fun deleteById(id: Long): Boolean {
        val rowsDeleted = dsl.deleteFrom(DSL.table("human_being"))
            .where(DSL.field("id").eq(id))
            .execute()
        return rowsDeleted > 0
    }
}
