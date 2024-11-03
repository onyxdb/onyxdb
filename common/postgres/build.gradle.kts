plugins {
    id("onyxdb-java-library-conventions")
}

dependencies {
    api(libs.postgresql.postgresql)
    api(libs.springBoot.starterJdbc)
    api(libs.flywaydb.flywayCore)
    runtimeOnly(libs.flywaydb.flywayDatabasePostgresql)
}
