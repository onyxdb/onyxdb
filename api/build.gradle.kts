plugins {
	java
	id("onyxdb-java-spring-app-conventions")
	id("onyxdb-jooq-manual-conventions")
	alias(libs.plugins.openapiGenerator)
	alias(libs.plugins.lombok)
}

configurations.all {
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
	implementation(libs.springBoot.starterWeb)
	implementation(libs.springBoot.starterLog4j2)

	// Deps for openapi generator
	implementation(libs.swaggerCoreV3.swaggerAnnotations)
	implementation(libs.javaxAnnotation.annotionApi)
	implementation(libs.javaxValidation.validationApi)

	testImplementation(libs.springBoot.starterTest)
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$projectDir/src/main/resources/openapi/onyx-api.yaml")
	outputDir.set("$buildDir/generated/openapi")
	apiPackage.set("${project.group}.generated.api.apis")
	modelPackage.set("${project.group}.generated.api.models")
	configOptions = mapOf(
		"dateLibrary" to "java8-localdatetime",
		"interfaceOnly" to "true",
		"useTags" to "true",
		"skipDefaultInterface" to "true",
		"openApiNullable" to "false",
		"hideGenerationTimestamp" to "true"
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

tasks.compileJava {
	dependsOn(tasks.openApiGenerate)
}
