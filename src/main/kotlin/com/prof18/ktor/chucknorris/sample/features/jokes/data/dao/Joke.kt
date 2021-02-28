package com.prof18.ktor.chucknorris.sample.features.jokes.data.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.datetime

object JokeTable: IdTable<String>(name = "joke") {
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val value = text("value")

    override val id: Column<EntityID<String>> = varchar("joke_id", 255).entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

}

class Joke(id: EntityID<String>): Entity<String>(id) {
    companion object: EntityClass<String, Joke>(JokeTable)

    var createdAt by JokeTable.createdAt
    var updatedAt by JokeTable.updatedAt
    var value by JokeTable.value
}