package com.prof18.ktor.chucknorris.sample.database

interface DatabaseFactory {
    fun connect()
    fun close()
}