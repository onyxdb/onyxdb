plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // Deps for onyxdb-java-spring-app-conventions plugin
    implementation(libs.springBoot.gradlePlugin)
}
