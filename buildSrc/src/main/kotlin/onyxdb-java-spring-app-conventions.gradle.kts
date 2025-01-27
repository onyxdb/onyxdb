plugins {
    id("onyxdb-java-app-conventions")
    id("org.springframework.boot")
}

tasks.jar {
    // Disables generating plain jar
    enabled = false
}

tasks.bootJar {
    destinationDirectory.set(file("${layout.buildDirectory}/export"))
}
