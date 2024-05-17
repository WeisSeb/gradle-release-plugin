import java.util.*

plugins {
    id("build.common.artifactory")
    id("build.common.kotlin-conventions")
    `java-gradle-plugin`
}

group = "io.github.simonhauck.release"

version = readVersionFromFile(rootProject.file("../version.properties"))

dependencies {
    implementation(libs.ztExec)
    implementation(libs.kotlinLogging)
    implementation(libs.arrowKt)

    testImplementation(gradleTestKit())
    testImplementation(libs.bundles.junit)
}

gradlePlugin {
    plugins {
        create("release-plugin") {
            id = "io.github.simonhauck.release"
            implementationClass = "io.github.simonhauck.release.plugin.ReleasePlugin"
        }
    }
}

private fun readVersionFromFile(file: File): String = readProperties(file).getProperty("version")

private fun readProperties(propertiesFile: File) =
    Properties().apply { propertiesFile.inputStream().use { load(it) } }
