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
	implementation(project(":onyxdbCommon:postgres"))

	implementation(libs.springBoot.starterWeb)
	implementation(libs.springBoot.starterLog4j2)
	implementation(libs.springBoot.starterValidation)
	implementation(libs.apacheCommons.commonsLang3)
	implementation(libs.springDoc.springdocOpenapiStarterWebmvcUi)

	// Deps for openapi generator
	implementation(libs.swaggerCoreV3.swaggerAnnotations)
	implementation(libs.javaxAnnotation.annotionApi)
	implementation(libs.javaxValidation.validationApi)

	testImplementation(libs.springBoot.starterTest)
	testImplementation(libs.testcontainers.junitJupiter)
	testImplementation(libs.testcontainers.postgresql)
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$projectDir/src/main/resources/openapi/scheme.yaml")
	outputDir.set("$projectDir/generated/openapi")
	apiPackage.set("${project.group}.onyxdbApi.generated.openapi.apis")
	modelPackage.set("${project.group}.onyxdbApi.generated.openapi.models")
	configOptions = mapOf(
		"dateLibrary" to "java8-localdatetime",
		"interfaceOnly" to "true",
		"useTags" to "true",
		"skipDefaultInterface" to "true",
		"openApiNullable" to "false",
		"hideGenerationTimestamp" to "true",
		"useJakartaEe" to "true",
		"useOptional" to "true"
	)
	globalProperties = mapOf(
		"apis" to "",
		"models" to ""
	)
}

sourceSets {
	main {
		java {
			srcDirs("$projectDir/generated/openapi/src/main/java")
		}
	}
}

tasks.named(CustomTasksConfig.ONYXDB_GENERATE_ALL_CODEGEN).configure {
	dependsOn(tasks.openApiGenerate)
}
