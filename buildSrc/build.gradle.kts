plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    // Deps for onyxdb-java-spring-app-conventions plugin
    implementation(libs.springBoot.gradlePlugin)

    // Deps for onyxdb-jooq-manual-conventions plugin
    implementation(libs.studerJooq.studerJooqGradlePlugin)
    implementation(libs.flywaydb.flywayGradlePlugin)
    implementation(libs.flywaydb.flywayDatabasePostgresql)
    implementation(libs.testcontainers.postgresql)
}
