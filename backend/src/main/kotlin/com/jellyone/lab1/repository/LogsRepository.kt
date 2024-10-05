package com.jellyone.lab1.repository

import com.jellyone.lab1.domain.logs.CarsLogs
import com.jellyone.lab1.domain.logs.HumanBeingsLogs
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class LogsRepository(private val dsl: DSLContext) {

    fun humansLogsSave(log: HumanBeingsLogs) {
        val record = dsl.insertInto(DSL.table("human_beings_logs"))
            .set(DSL.field("human_id"), log.humanId)
            .set(DSL.field("action"), log.action.toString())
            .set(DSL.field("username"), log.username)
            .set(DSL.field("date"), log.date)
            .execute()
    }

    fun carsLogsSave(log: CarsLogs) {
        val record = dsl.insertInto(DSL.table("cars_logs"))
            .set(DSL.field("car_id"), log.carId)
            .set(DSL.field("action"), log.action.toString())
            .set(DSL.field("username"), log.username)
            .set(DSL.field("date"), log.date)
            .execute()
    }
}