package com.prof18.ktor.chucknorris.sample.testutils.database

import com.prof18.ktor.chucknorris.sample.features.jokes.data.dao.JokeTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object SchemaDefinition {

    fun createSchema() {
        transaction {
            SchemaUtils.create(JokeTable)
        }
    }
}