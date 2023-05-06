package com.prof18.ktor.chucknorris.sample

import ch.qos.logback.classic.Logger
import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.config.setupConfig
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactory
import com.prof18.ktor.chucknorris.sample.di.appModule
import com.prof18.ktor.chucknorris.sample.features.jokes.resource.jokeEndpoint
import com.prof18.ktor.chucknorris.sample.jobs.JobFactory
import com.prof18.ktor.chucknorris.sample.jobs.JobSchedulerManager
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
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
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
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
        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        static {
            resource("doc/swagger.yml", "doc/swagger.yml")
            resource("doc", "doc/index.html")
        }
    }
}


