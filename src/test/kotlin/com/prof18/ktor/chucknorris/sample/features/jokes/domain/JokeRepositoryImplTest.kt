package com.prof18.ktor.chucknorris.sample.features.jokes.domain

import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSource
import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSourceImpl
import com.prof18.ktor.chucknorris.sample.features.jokes.data.dao.Joke
import com.prof18.ktor.chucknorris.sample.testutils.database.DatabaseFactoryForUnitTest
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.time.LocalDateTime

class JokeRepositoryImplTest : KoinTest {

    private lateinit var databaseFactory: DatabaseFactoryForUnitTest

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        // Your KoinApplication instance here
        modules(module {
            singleBy<JokeLocalDataSource, JokeLocalDataSourceImpl>()
            singleBy<JokeRepository, JokeRepositoryImpl>()
        })
    }

    private val jokeRepository: JokeRepository by inject()

    @Before
    fun setup() {
        databaseFactory = DatabaseFactoryForUnitTest()
        databaseFactory.connect()
    }

    @After
    fun tearDown() {
        databaseFactory.close()
    }

    @Test
    fun `getRandomJoke returns data correctly`() = runBlocking {
        // Setup
        val joke = transaction {
            Joke.new("joke_1") {
                this.value = "Chuck Norris tests are always green"
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }
        }

        // Act
        val randomJoke = jokeRepository.getRandomJoke()

        // Assert
        assertEquals(transaction { joke.id.value }, randomJoke.jokeId)
        assertEquals(transaction { joke.value }, randomJoke.jokeContent)
    }
}