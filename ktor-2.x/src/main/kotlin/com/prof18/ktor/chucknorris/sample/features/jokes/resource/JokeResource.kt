package com.prof18.ktor.chucknorris.sample.features.jokes.resource

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import io.ktor.server.application.call
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
@Location("joke")
class JokeEndpoint {

    @Location("/random")
    class Random(val parent: JokeEndpoint)

    @Location("/watch/{name}")
    class Watch(val name: String, val parent: JokeEndpoint)
}

@KtorExperimentalLocationsAPI
fun Route.jokeEndpoint() {

    val jokeRepository by inject<JokeRepository>()

    get<JokeEndpoint.Random> {
        call.respond(jokeRepository.getRandomJoke())
    }

    post<JokeEndpoint.Watch> {  apiCallParams ->
        val name = apiCallParams.name
        jokeRepository.watch(name)
        call.respond("Ok")
    }

}
