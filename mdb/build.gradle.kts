plugins {
	alias(libs.plugins.onyxdb.javaSpringAppConventions)
	alias(libs.plugins.onyxdb.jooqManualConventions)
	alias(libs.plugins.openapiGenerator)
	alias(libs.plugins.lombok)
}

configurations.all {
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
	implementation(project(":common:postgres"))

	implementation(libs.springBoot.starterWeb)
	implementation(libs.springBoot.starterLog4j2)
	implementation(libs.springBoot.starterValidation)
	implementation(libs.apacheCommons.commonsLang3)
	implementation(libs.springDoc.springdocOpenapiStarterWebmvcUi)

	// openapi generator
	implementation(libs.swaggerCoreV3.swaggerAnnotations)
	implementation(libs.javaxAnnotation.annotionApi)
	implementation(libs.javaxValidation.validationApi)

	implementation(libs.orgJetbrains.annotations)

	implementation("io.fabric8:kubernetes-client:7.1.0")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

	testImplementation(libs.springBoot.starterTest)
	testImplementation(libs.springBoot.starterTestcontainers)
	testImplementation(libs.testcontainers.junitJupiter)
	testImplementation(libs.testcontainers.postgresql)
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$projectDir/src/main/resources/openapi/scheme.yaml")
	outputDir.set("$buildDir/generated/openapi")
	apiPackage.set("${project.group}.mdb.generated.openapi.apis")
	modelPackage.set("${project.group}.mdb.generated.openapi.models")
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

tasks.named(CustomTasksConfig.ONYXDB_GENERATE_ALL_CODEGEN).configure {
	dependsOn(tasks.openApiGenerate)
}

tasks.compileJava {
	dependsOn(tasks.openApiGenerate)
}

tasks.openApiGenerate {
	dependsOn(tasks.clean)
}

tasks.generateJooq {
	dependsOn(tasks.clean)
}
