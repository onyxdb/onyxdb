plugins {
    id("java")
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

tasks.register(CustomTasksConfig.ONYXDB_GENERATE_ALL_CODEGEN) {
    group = CustomTasksConfig.ONYXDB_GROUP
}
