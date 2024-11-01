plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // Deps for onyxdb-java-spring-app-conventions plugin
    implementation(libs.springBoot.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("xor-openapi-conventions") {
            id = "xor-openapi-conventions"
            implementationClass = "ru.xority.conventions.openapi.XorOpenapiConventionsPlugin"
        }
    }
}
