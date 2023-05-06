package com.prof18.ktor.chucknorris.sample.jobs

import com.prof18.ktor.chucknorris.sample.features.jokes.domain.JokeRepository
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle
import kotlin.reflect.jvm.jvmName

class JobFactory(
    private val jokeRepository: JokeRepository
): JobFactory {

    override fun newJob(bundle: TriggerFiredBundle?, scheduler: Scheduler?): Job {
        if (bundle != null) {
            val jobClass = bundle.jobDetail.jobClass
            if (jobClass.name == RandomJokeJob::class.jvmName) {
                return RandomJokeJob(jokeRepository)
            }
        }
        throw NotImplementedError("Job Factory error")
    }
}