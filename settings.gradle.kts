rootProject.name = "kotlin-analysis-server"

pluginManagement {
    val buildKotlinVersion: String by settings

    plugins {
        kotlin("jvm") version buildKotlinVersion apply false
    }

    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap/")
        maven("https://www.jetbrains.com/intellij-repository/snapshots")
    }
}
