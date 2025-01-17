plugins {
    alias(libs.plugins.onyxdb.javaSpringAppConventions)
    alias(libs.plugins.onyxdb.jooqManualConventions)
    alias(libs.plugins.lombok)

    id("io.spring.dependency-management")
    id("com.netflix.dgs.codegen") version "7.0.3"
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

extra["netflixDgsVersion"] = "10.0.1"

dependencies {
    implementation(project(":common:postgres"))

//    NETFLIX START
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    NETFLIX END

    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)

    testImplementation(libs.springBoot.starterTest)
    testImplementation(libs.testcontainers.junitJupiter)
    testImplementation(libs.testcontainers.postgresql)
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${property("netflixDgsVersion")}")
    }
}

tasks.generateJava {
    schemaPaths.add("${projectDir}/src/main/resources/schema")
    packageName = "com.onyxdb.idm.codegen"
    generateClient = true
}
