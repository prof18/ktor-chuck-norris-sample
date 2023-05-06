package com.prof18.ktor.chucknorris.sample.features.jokes.resource

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
@Location("joke")
class JokeEndpoint {

    @Location("/random")
    class Random(val parent: JokeEndpoint)
}

@KtorExperimentalLocationsAPI
fun Route.jokeEndpoint() {

    val jokeRepository by inject<JokeRepository>()

    get<JokeEndpoint.Random> {
        call.respond(jokeRepository.getRandomJoke())
    }

}
