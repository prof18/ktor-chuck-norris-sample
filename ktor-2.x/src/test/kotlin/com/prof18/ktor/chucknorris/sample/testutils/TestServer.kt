package com.prof18.ktor.chucknorris.sample.testutils

import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.config.DatabaseConfig
import com.prof18.ktor.chucknorris.sample.config.ServerConfig
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactory
import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSource
import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSourceImpl
import com.prof18.ktor.chucknorris.sample.jobs.JobSchedulerManager
import com.prof18.ktor.chucknorris.sample.module
import com.prof18.ktor.chucknorris.sample.testutils.database.DatabaseFactoryForServerTest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun MapApplicationConfig.createConfigForTesting() {
    // Server config
    put("ktor.server.isProd", "false")

    // Database Config
    put("ktor.database.driverClass", "org.h2.Driver")
    put("ktor.database.url", "jdbc:h2:mem:;DATABASE_TO_UPPER=false;MODE=MYSQL")
    put("ktor.database.user", "root")
    put("ktor.database.password", "password")
    put("ktor.database.maxPoolSize", "1")
}

fun getAppConfigForUnitTest(): AppConfig {
    return AppConfig().apply {
        databaseConfig = DatabaseConfig(
            driverClass = "org.h2.Driver",
            url = "jdbc:h2:mem:;DATABASE_TO_UPPER=false;MODE=MYSQL",
            user = "root",
            password = "password",
            maxPoolSize = 1
        )
        serverConfig = ServerConfig(isProd = false)
    }
}

fun withTestServer(koinModules: List<Module> = listOf(appTestModule), block: suspend (HttpClient) -> Unit) {
    testApplication {
        application {
            module(testing = true, koinModules = koinModules)
        }

        this.environment {
            config = MapApplicationConfig().apply {
                createConfigForTesting()
            }
        }

        // To set up DI and all the stuff
        client.get("/")

        block(client)
    }
}

val appTestModule = module {
    singleOf(::AppConfig)
    singleOf(::JobSchedulerManager)
    single<DatabaseFactory> { DatabaseFactoryForServerTest(get()) }
    single<JokeLocalDataSource> { JokeLocalDataSourceImpl() }
}


