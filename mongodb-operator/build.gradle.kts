plugins {
    alias(libs.plugins.onyxdb.javaSpringAppConventions)
    alias(libs.plugins.lombok)
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)
    testImplementation(libs.springBoot.starterTest)

    // Deps for k8s operator
    implementation(libs.javaOperatorSdk.operatorFrameworkSpringBootStarter)
    annotationProcessor(libs.ioFabric8.crdGeneratorApt)
    implementation(libs.orgBouncycastle.bcprovJdk18on)
    implementation(libs.orgBouncycastle.bcpkixJdk18on)
    testImplementation(libs.javaOperatorSdk.operatorFrameworkSpringBootStarterTest)
}
