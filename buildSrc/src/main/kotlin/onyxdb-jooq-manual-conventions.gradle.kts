import nu.studer.gradle.jooq.JooqGenerate
import org.testcontainers.containers.PostgreSQLContainer

plugins {
    id("onyxdb-java-app-conventions")
    id("org.flywaydb.flyway")
    id("nu.studer.jooq")
}

dependencies {
    // Version should be synced with libs.versions.toml
    jooqGenerator("org.postgresql:postgresql:42.7.2")
}
//val permittedTasks: Set<String> = setOf(
//    JooqConfig.GENERATE_JOOQ_TASK,
//    CustomTasksConfig.ONYXDB_GENERATE_ALL_CODEGEN,
//    "build",
//    "test",
//    "testClasses",
//    "mdb:classes",
//)


//val triggeredTaskNames: MutableList<String> = project.gradle.startParameter.taskNames

val psqlContainer: PostgreSQLContainer<Nothing>? =
//    if (permittedTasks.any { it in triggeredTaskNames }) {
        PostgreSQLContainer<Nothing>(JooqConfig.POSTGRES_DOCKER_IMAGE).apply {
            start()
        }
//    } else {
//        null
//    }


flyway {
    locations = arrayOf(
        "filesystem:" + projectDir.absolutePath + "/src/main/resources/db/migration"
    )
    url = psqlContainer?.jdbcUrl
    user = psqlContainer?.username
    password = psqlContainer?.password
}

jooq {
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.ERROR
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = psqlContainer?.jdbcUrl
                    user = psqlContainer?.username
                    password = psqlContainer?.password
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        isIncludeIndexes = true
                        excludes = "flyway.*"
                    }
                    target.apply {
                        packageName = "${project.group}.${project.name}.generated.jooq"
                        directory = "$buildDir/generated/jooq/src/main/java"
                    }
                }
            }
        }
    }
}

tasks.register(JooqConfig.STOP_PSQL_CONTAINER) {
    doLast {
        psqlContainer?.stop()
    }
}

tasks.named<JooqGenerate>(JooqConfig.GENERATE_JOOQ_TASK) {
    dependsOn(JooqConfig.FLYWAY_MIGRATE_TASK)
    finalizedBy(JooqConfig.STOP_PSQL_CONTAINER)
}

tasks.compileJava {
    dependsOn(JooqConfig.GENERATE_JOOQ_TASK)
}

tasks.configureEach {
    // Fixes error: Task ':generateEffectiveLombokConfig' uses this output of task ':generateJooq' without declaring an explicit or implicit dependency.
    if (name == "generateEffectiveLombokConfig") {
        mustRunAfter(JooqConfig.GENERATE_JOOQ_TASK)
    }
}

gradle.taskGraph.whenReady {
    if (!gradle.taskGraph.allTasks.any { it.project.name == project.name && it.name == JooqConfig.GENERATE_JOOQ_TASK }) {
        psqlContainer?.stop()
    }
}
