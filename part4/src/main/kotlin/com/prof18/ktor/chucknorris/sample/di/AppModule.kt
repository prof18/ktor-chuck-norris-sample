package com.prof18.ktor.chucknorris.sample.di

import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactory
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactoryImpl
import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSource
import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSourceImpl
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepositoryImpl
import org.koin.dsl.module
import org.koin.dsl.single

val appModule = module {
    // Backend Config
    single<AppConfig>()
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }
    single<JokeLocalDataSource> { JokeLocalDataSourceImpl() }
    single<JokeRepository> { JokeRepositoryImpl(get()) }

}
