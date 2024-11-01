plugins {
    java
}

group = ProjectConfig.GROUP

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(ProjectConfig.JAVA_VERSION)
    }
}

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}
