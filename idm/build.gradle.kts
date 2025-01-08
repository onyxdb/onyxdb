plugins {
    alias(libs.plugins.onyxdb.javaSpringAppConventions)
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)
    testImplementation(libs.springBoot.starterTest)
}
