package com.prof18.ktor.chucknorris.sample.features.jokes.resource

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject

@Resource("joke")
class JokeEndpoint {

    @Resource("/random")
    class Random(val parent: JokeEndpoint = JokeEndpoint())

    @Resource("/watch/{name}")
    class Watch(val name: String, val parent: JokeEndpoint = JokeEndpoint())
}

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
