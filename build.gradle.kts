plugins {
	alias(libs.plugins.onyxdb.javaSpringAppConventions)
	alias(libs.plugins.onyxdb.jooqManualConventions)
	alias(libs.plugins.openapiGenerator)
	alias(libs.plugins.lombok)
	alias(libs.plugins.netflixDGSCodegen)
	alias(libs.plugins.spring.dependencyManagement)
}

buildscript {
	dependencies {
		classpath("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml") {
			version { strictly("2.14.2") }
		}
	}
}

//extra["netflixDgsVersion"] = libs.versions.netflix.dgs.version
extra["netflixDgsVersion"] = "10.0.1" // Без этого не работает ниже dependencyManagement

dependencyManagement {
	imports {
		mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${property("netflixDgsVersion")}")
	}
}

configurations.all {
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
	implementation(project(":common:postgres"))

	implementation(libs.springBoot.starterWeb)
	implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.5")
	implementation(libs.springBoot.starterLog4j2)
	implementation(libs.springBoot.starterValidation)
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.3.5")
	implementation(libs.apacheCommons.commonsLang3)
	implementation(libs.springDoc.springdocOpenapiStarterWebmvcUi)

	// openapi generator
	implementation(libs.swaggerCoreV3.swaggerAnnotations)
	implementation(libs.javaxAnnotation.annotionApi)
	implementation(libs.javaxValidation.validationApi)
	implementation("javax.validation:validation-api:2.0.0.Final")

	implementation(libs.orgJetbrains.annotations)

	implementation("io.fabric8:kubernetes-client:7.1.0")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.18.3")

	implementation("redis.clients:jedis:5.2.0")
	implementation("com.clickhouse:clickhouse-jdbc:0.7.1")
	implementation("org.lz4:lz4-java:1.8.0")

	// clickhouse
	implementation("com.clickhouse:clickhouse-jdbc:0.7.1")
	implementation("org.lz4:lz4-java:1.8.0")
	implementation("org.flywaydb:flyway-database-clickhouse:10.16.3")
	implementation(libs.yandex.clickhouseJdbc)

	// schedlock
	implementation("net.javacrumbs.shedlock:shedlock-spring:6.3.1")
	implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:6.3.1")

	// security
	implementation(libs.springSecurity.core)
	implementation(libs.springSecurity.config)
	implementation(libs.springSecurity.crypto)
	implementation(libs.springSecurity.web)

	// https://mvnrepository.com/artifact/org.aspectj/aspectjweaver
	implementation("org.aspectj:aspectjweaver:1.9.24")

	// netflix
	implementation(libs.netflix.graphql.dgs.codegen)
	testImplementation(libs.netflix.graphql.dgs.starterTest)
	testRuntimeOnly(libs.junit.platformLauncher)

	// spring data redis
	implementation(libs.springBoot.starterDataRedis)
	implementation(libs.springData.dataRedis)

	// jwt
	implementation(libs.ioJsonwebtoken.jjwtApi)
	implementation(libs.ioJsonwebtoken.jjwtImpl)
	implementation(libs.ioJsonwebtoken.jjwtJackson)

	testImplementation(libs.springBoot.starterTest)
	testImplementation(libs.springBoot.starterTestcontainers)
	testImplementation(libs.testcontainers.junitJupiter)
	testImplementation(libs.testcontainers.postgresql)
	testImplementation("org.testcontainers:clickhouse:1.20.6")
	testImplementation("com.redis:testcontainers-redis:2.2.2")
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$projectDir/src/main/resources/openapi/schema.yaml")
	outputDir.set("$buildDir/generated/openapi")
	apiPackage.set("${project.group}.platform.generated.openapi.apis")
	modelPackage.set("${project.group}.platform.generated.openapi.models")
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

sourceSets {
	main {
		java {
			srcDirs("$buildDir/generated/openapi/src/main/java")
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

tasks.compileJava {
	dependsOn(tasks.openApiGenerate)
}

tasks.openApiGenerate {
//	dependsOn(tasks.clean)
}

tasks.generateJooq {
//	dependsOn(tasks.clean)
}
