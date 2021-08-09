package com.prof18.ktor.chucknorris.sample.features.jokes.domain

import com.prof18.ktor.chucknorris.sample.features.jokes.data.JokeLocalDataSource
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.mapper.toDTO
import com.prof18.ktor.chucknorris.sample.features.jokes.domain.model.JokeDTO
import com.prof18.ktor.chucknorris.sample.jobs.JobSchedulerManager
import com.prof18.ktor.chucknorris.sample.jobs.RandomJokeJob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.quartz.*

class JokeRepositoryImpl(
    private val jokeLocalDataSource: JokeLocalDataSource,
    private val jobSchedulerManager: JobSchedulerManager
) : JokeRepository {

    override suspend fun getRandomJoke(): JokeDTO {
        val jokeDTO = newSuspendedTransaction {
            val allJokes = jokeLocalDataSource.getAllJokes()
            val randomJoke = allJokes.random()
            return@newSuspendedTransaction randomJoke.toDTO()
        }
        return jokeDTO
    }

    override suspend fun watch(name: String) {
        // Schedule a cron every two days to renew the subscription
        val jobId = "chuck-watch-job-for-name-$name"
        val triggerId = "chuck-watch-trigger-for-name-$name"

        // If a job exists, delete it!
        val jobScheduler = jobSchedulerManager.scheduler
        val jobKey = JobKey.jobKey(jobId, RandomJokeJob.WATCH_JOB_GROUP)
        jobScheduler.deleteJob(jobKey)

        val job: JobDetail = JobBuilder.newJob(RandomJokeJob::class.java)
            .withIdentity(jobId, RandomJokeJob.WATCH_JOB_GROUP)
            .usingJobData(RandomJokeJob.JOB_MAP_NAME_ID_KEY, name)
            .build()

        val trigger: Trigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerId, RandomJokeJob.WATCH_JOB_GROUP)
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    // every minute
                    .withIntervalInMinutes(1)
                    .repeatForever()
            )
            .build()

        // Tell quartz to schedule the job using our trigger
        jobSchedulerManager.scheduler.scheduleJob(job, trigger)
    }

    override fun getChuckGreeting(name: String): String {
        return "Hi $name, remember that Chuck Norris is watching you 👀"
    }
}