plugins {
    alias(libs.plugins.onyxdb.javaSpringAppConventions)
    alias(libs.plugins.onyxdb.jooqManualConventions)
    alias(libs.plugins.lombok)
    alias(libs.plugins.openapiGenerator)
    alias(libs.plugins.netflixDGSCodegen)

    id("io.spring.dependency-management")
}


configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

//extra["netflixDgsVersion"] = libs.versions.netflixDgsVersion
extra["netflixDgsVersion"] = "10.0.1"

dependencies {
    implementation(project(":common:postgres"))

    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)
    implementation(libs.springBoot.starterValidation)
    implementation(libs.apacheCommons.commonsLang3)
    implementation(libs.springDoc.springdocOpenapiStarterWebmvcUi)

    // NETFLIX START
    implementation(libs.netflix.graphql.dgs.codegen)
    testImplementation(libs.netflix.graphql.dgs.starterTest)
    testRuntimeOnly(libs.junit.platformLauncher)
    // NETFLIX END

    // openapi generator START
    implementation(libs.swaggerCoreV3.swaggerAnnotations)
    implementation(libs.javaxAnnotation.annotionApi)
    implementation(libs.javaxValidation.validationApi)
    // openapi generator END

    testImplementation(libs.springBoot.starterTest)
    testImplementation(libs.testcontainers.junitJupiter)
    testImplementation(libs.testcontainers.postgresql)
}


openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/openapi/scheme.yaml")
    outputDir.set("$projectDir/generated/openapi")
    apiPackage.set("${project.group}.idm.generated.openapi.apis")
    modelPackage.set("${project.group}.idm.generated.openapi.models")
    configOptions = mapOf(
        "dateLibrary" to "java8-localdatetime",
        "interfaceOnly" to "true",
        "useTags" to "true",
        "skipDefaultInterface" to "true",
        "openApiNullable" to "false",
        "hideGenerationTimestamp" to "true",
        "useJakartaEe" to "true"
    )
    globalProperties = mapOf(
        "apis" to "",
        "models" to ""
    )
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${property("netflixDgsVersion")}")
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                "$projectDir/generated/openapi/src/main/java"
            )
        }
    }
}

tasks.generateJava {
    schemaPaths.add("${projectDir}/src/main/resources/schema")
    packageName = "com.onyxdb.idm.codegen"
    generateClient = true
}

tasks.named(CustomTasksConfig.ONYXDB_GENERATE_ALL_CODEGEN).configure {
    dependsOn(tasks.openApiGenerate)
}
