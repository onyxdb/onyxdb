import io.fabric8.crd.generator.collector.CustomResourceCollector
import io.fabric8.crdv2.generator.CRDGenerationInfo
import io.fabric8.crdv2.generator.CRDGenerator
import org.gradle.api.internal.tasks.JvmConstants
import java.nio.file.Files

plugins {
    alias(libs.plugins.onyxdb.javaSpringAppConventions)
    alias(libs.plugins.lombok)
}

buildscript {
    dependencies {
        classpath(libs.ioFabric8.crdGeneratorApiV2)
        classpath(libs.ioFabric8.crdGeneratorCollector)
    }
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterLog4j2)
    testImplementation(libs.springBoot.starterTest)

    // k8s operator
    implementation(libs.javaOperatorSdk.operatorFrameworkSpringBootStarter)
    implementation(libs.orgBouncycastle.bcprovJdk18on)
    implementation(libs.orgBouncycastle.bcpkixJdk18on)
    testImplementation(libs.javaOperatorSdk.operatorFrameworkSpringBootStarterTest)
    // generateCrds task
    compileOnly(libs.ioFabric8.kubernetesClientApi)
}

// Based on https://github.com/fabric8io/kubernetes-client/blob/main/crd-generator/gradle/README.md
val generateCrdsTask = "generateCrds";

val generateCrdsForOutput = { outputDir: File ->
    val sourceSet = project.sourceSets["main"]

    val outputClassesDirs = sourceSet.output.classesDirs
    val filesToScan = listOf(outputClassesDirs).flatten()
    val outputClasspathElements = outputClassesDirs.map { f -> f.absolutePath }
    val compileClasspathElements = sourceSet.compileClasspath.map { f -> f.absolutePath }
    val classpathElements = listOf(outputClasspathElements, compileClasspathElements).flatten()

    val generatedManagedMongodbCrdFilename = "managed-mongodb.onyxdb.com-v1.yml"
    val renamedManagedMongodbCrdFilename = "managed-mongodb-crd.yaml"
    val renamedManagedMongodbCrdFile = outputDir.resolve(renamedManagedMongodbCrdFilename)
    val generatedManagedMongodbCrdFile = outputDir.resolve(generatedManagedMongodbCrdFilename)

    Files.createDirectories(outputDir.toPath())
    Files.deleteIfExists(renamedManagedMongodbCrdFile.toPath())

    val collector = CustomResourceCollector()
        .withParentClassLoader(Thread.currentThread().contextClassLoader)
        .withClasspathElements(classpathElements)
        .withFilesToScan(filesToScan)

    val crdGenerator = CRDGenerator()
        .customResourceClasses(collector.findCustomResourceClasses())
        .inOutputDir(outputDir)

    val crdGenerationInfo: CRDGenerationInfo = crdGenerator.detailedGenerate()

    crdGenerationInfo.crdDetailsPerNameAndVersion.forEach { (crdName, versionToInfo) ->
        println("Generated CRD $crdName:")
        versionToInfo.forEach { (version, info) -> println(" $version -> ${info.filePath}") }
    }

    Files.move(generatedManagedMongodbCrdFile.toPath(), renamedManagedMongodbCrdFile.toPath())
}

tasks.register(generateCrdsTask) {
    description = "Generate CRDs from compiled custom resource classes"
    group = "crd"

    dependsOn(JvmConstants.CLASSES_TASK_NAME)

    val outputDirs = listOf(
        file("$buildDir/classes/java/main/META-INF/fabric8"),
        file("$projectDir/k8s")
    )

    doLast {
        outputDirs.forEach { outputDir ->
            Files.createDirectories(outputDir.toPath())
            generateCrdsForOutput(outputDir)
        }
    }
}
