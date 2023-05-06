package com.prof18.ktor.chucknorris.sample.features.jokes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class JokeDTO(
    val jokeId: String,
    val jokeContent: String
)