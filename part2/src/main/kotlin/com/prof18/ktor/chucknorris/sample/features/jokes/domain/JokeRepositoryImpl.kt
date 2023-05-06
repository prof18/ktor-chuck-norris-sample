package com.prof18.ktor.chucknorris.sample.features.jokes.domain

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO

class JokeRepositoryImpl : JokeRepository {

    override suspend fun getRandomJoke(): JokeDTO {
        // TODO
        return JokeDTO(
            jokeId = "joke-id",
            jokeContent = "joke-content"
        )
    }
}