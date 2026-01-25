plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
    alias(libs.plugins.hilt)
    id("androidx.navigation.safeargs.kotlin")
    //id('kotlin-kapt')

    id("io.sentry.android.gradle")
}

android {
    namespace = "io.droidevs.counterapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.droidevs.counterapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("boolean", "IS_TEST", "true")
    }

    buildTypes {

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true

            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
        }
        getByName("debug") {
            java.srcDirs("src/debug/java")
        }
        getByName("release") {
            java.srcDirs("src/release/java")
        }
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.window)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work.alias)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.work.runtime)  // Check for latest; 2.9.x is current stable series
    implementation(libs.androidx.work.runtime.ktx)

    // Preferences DataStore (stores key-value pairs)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)

    // Optional - For Coroutines support (needed to read/write)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.google.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.gson)

    implementation(libs.androidx.lifecycle.process)

    implementation(libs.sentry.android)
    implementation(libs.sentry.android.fragment)
    implementation(libs.sentry.android.navigation)
}

sentry {
    org.set("droidevs")
    projectName.set("counter")

    // this will upload your source code to Sentry to show it as part of the stack traces
    // disable if you don't want to expose your sources
    includeSourceContext.set(true)
}
