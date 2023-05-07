package com.prof18.ktor.chucknorris.sample.di

import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactory
import com.prof18.ktor.chucknorris.sample.database.DatabaseFactoryImpl
import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSource
import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSourceImpl
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepositoryImpl
import com.prof18.ktor.chucknorris.sample.jobs.JobFactory
import com.prof18.ktor.chucknorris.sample.jobs.JobSchedulerManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // Backend Config
    singleOf(::AppConfig)
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }
    single<JokeLocalDataSource> { JokeLocalDataSourceImpl() }
    single<JokeRepository> { JokeRepositoryImpl(get(), get()) }
    singleOf(::JobSchedulerManager)
    singleOf(::JobFactory)
}
