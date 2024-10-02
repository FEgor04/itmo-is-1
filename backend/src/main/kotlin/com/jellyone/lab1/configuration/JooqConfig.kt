package com.jellyone.lab1.configuration

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class JooqConfig(private val dataSource: DataSource) {

    @Bean
    fun dslContext(): DSLContext {
        return DSL.using(dataSource, org.jooq.SQLDialect.POSTGRES)
    }
}