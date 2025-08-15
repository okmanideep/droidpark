// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register("whoAmI") {
    doLast {
        println("Gradle ${gradle.gradleVersion}")
        println("Java home: ${System.getProperty("java.home")}")
        println("Gradle user home: ${gradle.gradleUserHomeDir}")
        println("JVM args: ${System.getProperty("sun.jvm.args")}")
    }
}
