package com.prof18.ktor.chucknorris.sample.testutils.database

import com.prof18.ktor.chucknorris.sample.database.DatabaseFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DatabaseFactoryForUnitTest: DatabaseFactory {

    lateinit var source: HikariDataSource

    override fun close() {
        source.close()
    }

    override fun connect() {
        Database.connect(hikari())
        SchemaDefinition.createSchema()
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:;DATABASE_TO_UPPER=false;MODE=MYSQL"
        config.maximumPoolSize = 2
        config.isAutoCommit = true
        config.validate()
        source = HikariDataSource(config)
        return source
    }
}