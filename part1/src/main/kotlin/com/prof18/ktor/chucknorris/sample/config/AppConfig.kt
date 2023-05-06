package com.prof18.ktor.chucknorris.sample.config

import io.ktor.application.*
import org.koin.ktor.ext.inject

class AppConfig {
    lateinit var serverConfig: ServerConfig
    // Place here other configurations
}

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()

    // Server
    val serverObject = environment.config.config("ktor.server")
    val isProd = serverObject.property("isProd").getString().toBoolean()
    appConfig.serverConfig = ServerConfig(isProd)

}

data class ServerConfig(
    val isProd: Boolean
)
