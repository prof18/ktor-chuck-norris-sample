package com.prof18.ktor.chucknorris.sample.testutils

import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.module
import io.ktor.config.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.dsl.single

fun MapApplicationConfig.createConfigForTesting() {
    // Server config
    put("ktor.server.isProd", "false")
}


@KtorExperimentalLocationsAPI
fun withTestServer(koinModules: List<Module> = listOf(appTestModule), block: TestApplicationEngine.() -> Unit) {
    withTestApplication(
        {
            (environment.config as MapApplicationConfig).apply {
                createConfigForTesting()
            }
            module(testing = true, koinModules = koinModules)
        }, block
    )
}

val appTestModule = module {
    single<AppConfig>()
}


