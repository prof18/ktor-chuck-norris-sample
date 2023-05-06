package com.prof18.ktor.chucknorris.sample.features.jokes.domain

import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSource
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.mapper.toDTO
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class JokeRepositoryImpl(
    private val jokeLocalDataSource: JokeLocalDataSource
) : JokeRepository {

    override suspend fun getRandomJoke(): JokeDTO {
        val jokeDTO = newSuspendedTransaction {
            val allJokes = jokeLocalDataSource.getAllJokes()
            val randomJoke = allJokes.random()
            return@newSuspendedTransaction randomJoke.toDTO()
        }
        return jokeDTO
    }
}