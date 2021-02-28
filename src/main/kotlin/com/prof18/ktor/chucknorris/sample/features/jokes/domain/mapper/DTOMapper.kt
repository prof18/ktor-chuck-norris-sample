package com.prof18.ktor.chucknorris.sample.features.jokes.domain.mapper

import com.prof18.ktor.chucknorris.sample.features.jokes.data.dao.Joke
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO

fun Joke.toDTO(): JokeDTO {
    return JokeDTO(
        jokeId = this.id.value,
        jokeContent = this.value
    )
}