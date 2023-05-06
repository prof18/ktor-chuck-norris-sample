package com.prof18.ktor.chucknorris.sample.features.jokes.data

import com.prof18.ktor.chucknorris.sample.features.jokes.data.dao.Joke

interface JokeLocalDataSource {
    fun getAllJokes(): List<Joke>
}