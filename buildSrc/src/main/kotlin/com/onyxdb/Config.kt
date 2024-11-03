package com.onyxdb

object ProjectConfig {
    const val GROUP = "com.onyxdb"
    const val JAVA_VERSION = 21
}

object JooqConfig {
    const val GENERATE_JOOQ_TASK = "generateJooq"
    const val FLYWAY_MIGRATE_TASK = "flywayMigrate"
    const val POSTGRES_DOCKER_IMAGE = "postgres:14.4-alpine"
}
