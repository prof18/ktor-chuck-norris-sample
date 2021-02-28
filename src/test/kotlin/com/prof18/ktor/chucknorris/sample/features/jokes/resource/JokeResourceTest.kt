package com.prof18.ktor.chucknorris.sample.features.jokes.resource

import com.prof18.ktor.chucknorris.sample.features.jokes.data.dao.Joke
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepositoryImpl
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO
import com.prof18.ktor.chucknorris.sample.testutils.appTestModule
import com.prof18.ktor.chucknorris.sample.testutils.withTestServer
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.koin.test.AutoCloseKoinTest
import java.time.LocalDateTime

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class JokeResourceTest : AutoCloseKoinTest() {

    @Test
    fun `random joke api works correctly`() = withTestServer(
        koinModules = appTestModule.plus(
            module {
                // Just to showcase the possibility, in this case this dependency can be put in the base test module
                singleBy<JokeRepository, JokeRepositoryImpl>()
            }
        )
    ) {

        // Setup
        val joke = transaction {
            Joke.new("joke_1") {
                this.value = "Chuck Norris tests are always green"
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }
        }

        val href = application.locations.href(
            JokeEndpoint.Random(
                parent = JokeEndpoint()
            )
        )

        handleRequest(HttpMethod.Get, href).apply {
            assertEquals(HttpStatusCode.OK, response.status())

            val response = Json.decodeFromString<JokeDTO>(response.content!!)

            assertEquals(transaction { joke.id.value }, response.jokeId)
            assertEquals(transaction { joke.value }, response.jokeContent)
        }
    }
}