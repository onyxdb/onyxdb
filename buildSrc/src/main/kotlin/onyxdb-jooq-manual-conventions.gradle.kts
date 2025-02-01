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

val postgresContainer: PostgreSQLContainer<Nothing> =
    PostgreSQLContainer<Nothing>(JooqConfig.POSTGRES_DOCKER_IMAGE).apply {
        start()
    }

flyway {
    locations = arrayOf(
        "filesystem:" + projectDir.absolutePath + "/src/main/resources/db/migration"
    )
    url = postgresContainer.jdbcUrl
    user = postgresContainer.username
    password = postgresContainer.password
}

jooq {
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.ERROR
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = postgresContainer.jdbcUrl
                    user = postgresContainer.username
                    password = postgresContainer.password
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        isIncludeIndexes = false
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

tasks.named(JooqConfig.GENERATE_JOOQ_TASK).configure {
    dependsOn(tasks.named(JooqConfig.FLYWAY_MIGRATE_TASK))
    doLast {
        postgresContainer.stop()
    }
}

tasks.named(CustomTasksConfig.ONYXDB_GENERATE_ALL_CODEGEN).configure {
    dependsOn(JooqConfig.GENERATE_JOOQ_TASK)
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
