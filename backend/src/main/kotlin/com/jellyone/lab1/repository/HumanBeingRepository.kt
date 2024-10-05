package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.Coordinates
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.sql.Date
import java.time.LocalDate

@Repository
class HumanBeingRepository(private val dsl: DSLContext) {

    fun findById(id: Long): HumanBeing? {
        return dsl.selectFrom("human_being")
            .where(DSL.field("id").eq(id))
            .fetchOne()?.let { result ->
                createHumanBeingFromResult(result)
            }
    }

    fun findAll(
        page: Int,
        pageSize: Int,
        sortBy: HumanBeingFields,
        sortAsc: Boolean,
        name: String?,
        impactSpeedLT : Double?
    ): PaginatedResponse<HumanBeing> {
        val dslWhere =
            DSL.field("lower(name)")
                .contains(name?.lowercase() ?: "")
                .let {
                    if(impactSpeedLT != null) {
                        it.and(DSL.field("impact_speed").lessThan(impactSpeedLT))
                    } else {
                        it
                    }
                }
        val total = dsl.fetchCount(
            dsl.selectFrom("human_being")
                .where(dslWhere)
        )

        val records = dsl.select(
            DSL.field("id"),
            DSL.field("name"),
            DSL.field("x"),
            DSL.field("y"),
            DSL.field("creation_date"),
            DSL.field("real_hero"),
            DSL.field("has_toothpick"),
            DSL.field("car_id"),
            DSL.field("mood"),
            DSL.field("impact_speed"),
            DSL.field("weapon_type")
        )
            .from("human_being")
            .where(dslWhere)
            .orderBy(DSL.field(sortBy.dbName).let {
                if (sortAsc) it.asc() else it.desc()
            })
            .limit(pageSize)
            .offset((page - 1) * pageSize)
            .fetch()


        // Преобразуем каждую запись в HumanBeing
        val values = records.map { result ->
            val coordinates = Coordinates(
                x = result.get("x") as Double,
                y = result.get("y") as Double
            )

            val carId = result.get("car_id") as Long?
            val car = carId?.let { fetchCarById(it) } ?: throw IllegalArgumentException("Car ID cannot be null")


            HumanBeing(
                id = result.get("id") as Long,
                name = result.get("name") as String,
                coordinates = coordinates,
                creationDate = getLocalDateFromSqlDate(result.get("creation_date") as Date),
                realHero = result.get("real_hero") as Boolean,
                hasToothpick = result.get("has_toothpick") as Boolean,
                car = car,
                mood = result.get("mood")?.let { Mood.valueOf(it as String) },
                impactSpeed = result.get("impact_speed") as Long,
                weaponType = WeaponType.valueOf(result.get("weapon_type") as String)
            )
        }

        return PaginatedResponse(
            page,
            pageSize,
            total,
            values
        )
    }

    fun save(humanBeing: HumanBeing): HumanBeing {
        val record = dsl.insertInto(DSL.table("human_being"))
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

    private fun createHumanBeingFromResult(result: org.jooq.Record): HumanBeing {
        val coordinates = Coordinates(
            x = result.get("x") as Double,
            y = result.get("y") as Double
        )

        val carId = result.get("car_id") as Long?
        val car = carId?.let { fetchCarById(it) } ?: throw IllegalArgumentException("Car ID cannot be null")

        return HumanBeing(
            id = result.get("id") as Long,
            name = result.get("name") as String,
            coordinates = coordinates,
            creationDate = getLocalDateFromSqlDate(result.get("creation_date") as Date),
            realHero = result.get("real_hero") as Boolean,
            hasToothpick = result.get("has_toothpick") as Boolean,
            car = car,
            mood = result.get("mood")?.let { Mood.valueOf(it as String) },
            impactSpeed = result.get("impact_speed") as Long,
            weaponType = WeaponType.valueOf(result.get("weapon_type") as String)
        )
    }


    private fun fetchCarById(carId: Long): Car {
        return dsl.selectFrom("car")
            .where(DSL.field("id").eq(carId))
            .fetchOneInto(Car::class.java) ?: throw IllegalArgumentException("Car with id $carId not found")
    }

    private fun getLocalDateFromSqlDate(date: java.sql.Date): LocalDate {
        return date.toLocalDate()
    }

    enum class HumanBeingFields(val dbName: String, val entityName: String) {
        ID("id", "id"),
        NAME("name", "name"),
        CREATION_DATE("creation_date", "creationDate"),
        REAL_HERO("real_hero", "realHero"),
        HAS_TOOTHPICK("has_toothpick", "hasToothpick"),
        CAR_ID("car_id", "carId"),
        MOOD("mood", "mood"),
        IMPACT_SPEED("impact_speed", "impactSpeed"),
        WEAPON_TYPE("weapon_type", "weaponType"),
    }
}
