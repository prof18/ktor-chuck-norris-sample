package com.prof18.ktor.chucknorris.sample.features.jokes.fake

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO

class JokeRepositoryFake: JokeRepository {

    override suspend fun getRandomJoke(): JokeDTO {
        return JokeDTO(
            jokeId = "fake-id",
            jokeContent = "fake-content"
        )
    }
}