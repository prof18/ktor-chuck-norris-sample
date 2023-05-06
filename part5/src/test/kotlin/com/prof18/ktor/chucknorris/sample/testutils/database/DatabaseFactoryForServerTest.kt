package com.prof18.ktor.chucknorris.sample.testutils.database

import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DatabaseFactoryForServerTest(appConfig: AppConfig): DatabaseFactory {

    private val dbConfig = appConfig.databaseConfig

    override fun connect() {
        Database.connect(hikari())
        SchemaDefinition.createSchema()
    }

    override fun close() {
        // not needed
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = dbConfig.driverClass
        config.jdbcUrl = dbConfig.url
        config.maximumPoolSize = dbConfig.maxPoolSize
        config.isAutoCommit = true
        config.validate()
        return HikariDataSource(config)
    }
}