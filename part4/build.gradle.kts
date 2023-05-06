import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val mysql_connector_version: String by project
val hikaricp_version: String by project
val h2_version: String by project
val liquibase_core: String by project
val koin_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.30"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.30"
    id("org.liquibase.gradle") version "2.0.4"
}

group = "com.prof18.ktor.chucknorris.sample"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("mysql:mysql-connector-java:$mysql_connector_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")

    // Koin
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Liquibase
    liquibaseRuntime("org.liquibase:liquibase-core:$liquibase_core")
    liquibaseRuntime("mysql:mysql-connector-java:$mysql_connector_version")
    liquibaseRuntime("ch.qos.logback:logback-core:$logback_version")
    liquibaseRuntime("ch.qos.logback:logback-classic:$logback_version")
    liquibaseRuntime("javax.xml.bind:jaxb-api:2.2.4")

    // Testing
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("com.h2database:h2:$h2_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit4:$koin_version")
    testImplementation("junit:junit:4.12")

}

// Database migrations
val dbEnv: String by project.ext

val propertiesFile = file("local.properties")
val properties = Properties()
if (propertiesFile.exists()) {
    properties.load(propertiesFile.inputStream())
}

// gw update -PdbEnv=dev
liquibase {
    activities.register("dev") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/db/migration/migrations.xml",
            "url" to "jdbc:mysql://localhost:3308/chucknorris",
            "username" to "root",
            "password" to "password"
        )
    }

    activities.register("prod") {
        val url = properties.getProperty("liquibase.url") ?: System.getenv("LIQUIBASE_URL")
        val user = properties.getProperty("liquibase.user") ?: System.getenv("LIQUIBASE_USER")
        val pwd = properties.getProperty("liquibase.pwd") ?: System.getenv("LIQUIBASE_PWD")

        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "/resources/db/migration/migrations.xml",
            "url" to url,
            "username" to user,
            "password" to pwd
        )
    }
    runList = dbEnv
}