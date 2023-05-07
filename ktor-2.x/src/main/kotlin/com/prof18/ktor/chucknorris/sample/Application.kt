package com.prof18.ktor.chucknorris.sample

import ch.qos.logback.classic.Logger
import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.config.setupConfig
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactory
import com.prof18.ktor.chucknorris.sample.di.appModule
import com.prof18.ktor.chucknorris.sample.features.jokes.resource.jokeEndpoint
import com.prof18.ktor.chucknorris.sample.jobs.JobFactory
import com.prof18.ktor.chucknorris.sample.jobs.JobSchedulerManager
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.http.content.staticResources
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Locations
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.core.module.Module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

// /Users/marco/Workspace/Examples/ktor-chuck-norris-sample
// VM Options -DLOG_DEST=/Users/marco/Workspace/Examples/ktor-chuck-norris-sample/logs -DLOG_MAX_HISTORY=2
fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

/**
 * Please note that you can use any other name instead of *module*.
 * Also note that you can have more then one modules in your application.
 * */
@OptIn(KtorExperimentalLocationsAPI::class)
@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false, koinModules: List<Module> = listOf(appModule)) {

    install(Koin) {
        slf4jLogger()
        modules(koinModules)
    }

    setupConfig()

    val appConfig by inject<AppConfig>()

    if (!appConfig.serverConfig.isProd) {
        val root = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME) as Logger
        root.level = ch.qos.logback.classic.Level.TRACE
    }

    val databaseFactory by inject<DatabaseFactory>()
    databaseFactory.connect()

    if (!testing) {
        val jobSchedulerManager by inject<JobSchedulerManager>()
        val jobFactory by inject<JobFactory>()
        jobSchedulerManager.startScheduler()
        jobSchedulerManager.scheduler.setJobFactory(jobFactory)
    }

    install(ContentNegotiation) {
        json()
    }

    install(CallLogging) {
        level = Level.INFO
    }

    install(Locations)

    routing {
        jokeEndpoint()
        get("/") {
            call.respondText("This is a sample Ktor backend to get Chuck Norris jokes")
        }
    }

    routing {
        staticResources(
            remotePath = "/static",
            basePackage = "static"
        )

        staticResources(
            remotePath = "doc/swagger.yml",
            basePackage = null,
            index = "doc/swagger.yml"
        )
        staticResources(
            remotePath = "doc",
            basePackage = null,
            index = "doc/index.html"
        )
    }
}


