plugins {
    java
    id("onyxdb-java-spring-app-conventions")
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)

    testImplementation(libs.springBoot.starterTest)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
