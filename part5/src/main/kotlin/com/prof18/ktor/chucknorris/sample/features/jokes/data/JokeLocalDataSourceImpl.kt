package com.prof18.ktor.chucknorris.sample.features.jokes.data

import com.prof18.ktor.chucknorris.sample.features.jokes.data.dao.Joke
import com.prof18.ktor.chucknorris.sample.features.jokes.data.dao.JokeTable
import org.jetbrains.exposed.sql.selectAll

class JokeLocalDataSourceImpl : JokeLocalDataSource {

    override fun getAllJokes(): List<Joke> {
        val query = JokeTable.selectAll()
        return Joke.wrapRows(query).toList()
    }
}