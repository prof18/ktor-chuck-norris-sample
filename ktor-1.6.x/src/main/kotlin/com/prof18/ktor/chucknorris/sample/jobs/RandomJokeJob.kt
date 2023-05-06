package com.prof18.ktor.chucknorris.sample.jobs

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import org.quartz.Job
import org.quartz.JobExecutionContext

class RandomJokeJob(
    private val jokeRepository: JokeRepository
) : Job {

    override fun execute(context: JobExecutionContext?) {
        if (context == null) {
            return
        }

        val dataMap = context.jobDetail.jobDataMap

        val name: String? = try {
            dataMap.getString(JOB_MAP_NAME_ID_KEY)
        } catch (e: ClassCastException) {
            null
        }

        if (name != null) {
            val greetingMessage = jokeRepository.getChuckGreeting(name)

            println(greetingMessage)
        }
    }

    companion object {
        const val JOB_MAP_NAME_ID_KEY = "name"
        const val WATCH_JOB_GROUP = "WatchJob"

    }
}


