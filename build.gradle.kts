import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions") version "0.20.0"
}

buildscript {
    extra["assert_j_version"] = "3.11.1"
    extra["constraint_layout_version"] = "1.1.3"
    extra["dagger_version"] = "2.21"
    extra["kotlin_version"] = "1.3.20"
    extra["lifecycle_version"] = "1.1.1"
    extra["mockito_version"] = "1.6.0"
    extra["picasso_version"] = "2.71828"
    extra["room_version"] = "1.1.1"
    extra["rx_android_version"] = "2.1.0"
    extra["rx_java_version"] = "2.2.6"
    extra["support_lib_version"] = "28.0.0"

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlin_version"]}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview")
                    .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
                    .any { it.matches(candidate.version) }
                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}
