package com.prof18.ktor.chucknorris.sample.jobs

import com.prof18.ktor.chucknorris.sample.config.AppConfig
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.impl.StdSchedulerFactory
import java.util.*

class JobSchedulerManager(appConfig: AppConfig) {

    var scheduler: Scheduler

    init {
        val databaseConfig = appConfig.databaseConfig

        val props = Properties()
        props["org.quartz.scheduler.instanceName"] = "ChuckNorrisScheduler"
        props["org.quartz.threadPool.threadCount"] = "3"

        props["org.quartz.jobStore.dataSource"] = "mySql"
        props["org.quartz.dataSource.mySql.driver"] = databaseConfig.driverClass
        props["org.quartz.dataSource.mySql.URL"] = databaseConfig.url
        props["org.quartz.dataSource.mySql.user"] = databaseConfig.user
        props["org.quartz.dataSource.mySql.password"] = databaseConfig.password
        props["org.quartz.dataSource.mySql.maxConnections"] = "10"

        props["org.quartz.jobStore.class"] = "org.quartz.impl.jdbcjobstore.JobStoreTX"
        props["org.quartz.jobStore.driverDelegateClass"] = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate"
        props["org.quartz.jobStore.tablePrefix"] = "QRTZ_"


        props["org.quartz.plugin.triggHistory.class"] = "org.quartz.plugins.history.LoggingTriggerHistoryPlugin"
        props["org.quartz.plugin.triggHistory.triggerFiredMessage"] = """Trigger {1}.{0} fired job {6}.{5} at: {4, date, HH:mm:ss MM/dd/yyyy}"""
        props["org.quartz.plugin.triggHistory.triggerCompleteMessage"] = """Trigger {1}.{0} completed firing job {6}.{5} at {4, date, HH:mm:ss MM/dd/yyyy}"""

        val schedulerFactory: SchedulerFactory = StdSchedulerFactory(props)
        scheduler = schedulerFactory.scheduler
    }

    fun startScheduler() {
        scheduler.start()
    }
}
