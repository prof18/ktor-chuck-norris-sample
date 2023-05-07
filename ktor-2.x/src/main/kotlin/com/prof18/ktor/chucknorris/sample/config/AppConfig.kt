package com.prof18.ktor.chucknorris.sample.config

import io.ktor.server.application.Application
import org.koin.ktor.ext.inject

class AppConfig {
    lateinit var databaseConfig: DatabaseConfig
    lateinit var serverConfig: ServerConfig
    // Place here other configurations
}

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()

    // Server
    val serverObject = environment.config.config("ktor.server")
    val isProd = serverObject.property("isProd").getString().toBoolean()
    appConfig.serverConfig = ServerConfig(isProd)

    // Database
    val databaseObject = environment.config.config("ktor.database")
    val driverClass = databaseObject.property("driverClass").getString()
    val url = databaseObject.property("url").getString()
    val user = databaseObject.property("user").getString()
    val password = databaseObject.property("password").getString()
    val maxPoolSize = databaseObject.property("maxPoolSize").getString().toInt()
    appConfig.databaseConfig = DatabaseConfig(driverClass, url, user, password, maxPoolSize)
}

data class DatabaseConfig(
    val driverClass: String,
    val url: String,
    val user: String,
    val password: String,
    val maxPoolSize: Int
)

data class ServerConfig(
    val isProd: Boolean
)
