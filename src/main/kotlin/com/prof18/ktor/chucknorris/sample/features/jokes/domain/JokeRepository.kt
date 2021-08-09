package com.prof18.ktor.chucknorris.sample.features.jokes.domain

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO

interface JokeRepository {
    suspend fun getRandomJoke(): JokeDTO
    suspend fun watch(name: String)
    fun getChuckGreeting(name: String): String
}