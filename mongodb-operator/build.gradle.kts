plugins {
    alias(libs.plugins.onyxdb.javaSpringAppConventions)
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)

    implementation(libs.javaOperatorSdk.operatorFrameworkSpringBootStarter)
    annotationProcessor(libs.ioFabric8.crdGeneratorApt)
    implementation(libs.orgBouncycastle.bcprovJdk18on)
    implementation(libs.orgBouncycastle.bcpkixJdk18on)

    testImplementation(libs.springBoot.starterTest)
    testImplementation(libs.javaOperatorSdk.operatorFrameworkSpringBootStarterTest)
}
