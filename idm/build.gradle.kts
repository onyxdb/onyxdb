plugins {
    alias(libs.plugins.onyxdb.javaSpringAppConventions)
    alias(libs.plugins.onyxdb.jooqManualConventions)
    alias(libs.plugins.lombok)
    alias(libs.plugins.netflixDGSCodegen)

    id("io.spring.dependency-management")
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

extra["netflixDgsVersion"] = libs.versions.netflixDgsVersion

dependencies {
    implementation(project(":common:postgres"))

    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)

//    NETFLIX START
    implementation(libs.netflix.graphql.dgs.codegen)
    testImplementation(libs.netflix.graphql.dgs.starterTest)
    testRuntimeOnly(libs.junit.platformLauncher)
//    NETFLIX END


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
