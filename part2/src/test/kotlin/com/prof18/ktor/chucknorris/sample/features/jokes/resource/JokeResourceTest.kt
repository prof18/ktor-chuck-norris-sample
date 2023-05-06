package com.prof18.ktor.chucknorris.sample.features.jokes.resource

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO
import com.prof18.ktor.chucknorris.sample.features.jokes.fake.JokeRepositoryFake
import com.prof18.ktor.chucknorris.sample.testutils.appTestModule
import com.prof18.ktor.chucknorris.sample.testutils.withTestServer
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

@KtorExperimentalLocationsAPI
class JokeResourceTest : AutoCloseKoinTest() {

    @ExperimentalSerializationApi
    @Test
    fun `random joke api works correctly`() = withTestServer(
        koinModules = appTestModule.plus(
            module {
                // Just to showcase the possibility, in this case this dependency can be put in the base test module
                single<JokeRepository> { JokeRepositoryFake() }
            }
        )
    ) {

        val href = application.locations.href(
            JokeEndpoint.Random(
                parent = JokeEndpoint()
            )
        )

        handleRequest(HttpMethod.Get, href).apply {
            assertEquals(HttpStatusCode.OK, response.status())

            val response = Json.decodeFromString<JokeDTO>(response.content!!)

            assertEquals("fake-id", response.jokeId)
            assertEquals("fake-content", response.jokeContent)
        }
    }
}