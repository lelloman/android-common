plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.github.dcendents.android-maven")
}

group = "com.github.lelloman"

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        isEnabled = true
    }

    buildTypes {
        findByName("release")?.apply {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    val supportLibVersion = rootProject.extra["support_lib_version"]
    val kotlinVersion = rootProject.extra["kotlin_version"]
    val daggerVersion = rootProject.extra["dagger_version"]
    val lifecycleVersion = rootProject.extra["lifecycle_version"]
    val roomVersion = rootProject.extra["room_version"]
    val mockitoVersion = rootProject.extra["mockito_version"]
    val assertJVersion = rootProject.extra["assert_j_version"]
    val ui_test_utils_version = rootProject.extra["instrumented_test_utils_version"]

    // support
    api("com.android.support:appcompat-v7:$supportLibVersion")
    api("com.android.support:recyclerview-v7:$supportLibVersion")
    api("com.android.support:design:$supportLibVersion")
    api("com.android.support:cardview-v7:$supportLibVersion")
    api("com.android.support.constraint:constraint-layout:${rootProject.extra["constraint_layout_version"]}")
    api("android.arch.lifecycle:extensions:$lifecycleVersion")

    // kotlin
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    api("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    api("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // rx
    api("io.reactivex.rxjava2:rxandroid:${rootProject.extra["rx_android_version"]}")
    api("io.reactivex.rxjava2:rxjava:${rootProject.extra["rx_java_version"]}")

    // dagger
    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")

    implementation("com.squareup.picasso:picasso:${rootProject.extra["picasso_version"]}")

    testImplementation("junit:junit:4.12")
    testImplementation("android.arch.persistence.room:testing:$roomVersion")
    testImplementation("com.nhaarman:mockito-kotlin:$mockitoVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("android.arch.core:core-testing:$lifecycleVersion")

    androidTestImplementation("com.github.lelloman:android-instrumented-tests-utils:$ui_test_utils_version")
}
