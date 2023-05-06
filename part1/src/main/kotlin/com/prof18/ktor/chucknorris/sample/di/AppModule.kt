package com.prof18.ktor.chucknorris.sample.di

import com.prof18.ktor.chucknorris.sample.config.AppConfig
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepositoryImpl
import org.koin.dsl.module
import org.koin.dsl.single

val appModule = module {
    // Backend Config
    single<AppConfig>()
    single<JokeRepository> { JokeRepositoryImpl() }
}
