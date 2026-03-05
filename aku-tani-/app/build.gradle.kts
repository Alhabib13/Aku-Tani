plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.akutani"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.akutani"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "OPEN_WEATHER_KEY",
            "\"810c16a107b6360e234c160ccbbaa547\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildToolsVersion = "36.1.0 rc1"
}

dependencies {
        /* ================= CORE ================= */
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)

        /* ================= COMPOSE (PAKAI SATU BOM SAJA) ================= */
        implementation(platform(libs.androidx.compose.bom))

        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)

        // 🔴 INI WAJIB untuk KeyboardOptions
        implementation("androidx.compose.ui:ui-text")

        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)

        /* ================= VIEWMODEL ================= */
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

        /* ================= COROUTINES ================= */
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

        /* ================= NAVIGATION ================= */
        implementation("androidx.navigation:navigation-compose:2.8.5")

        /* ================= NETWORK ================= */
        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-gson:2.11.0")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

        /* ================= IMAGE ================= */
        implementation("io.coil-kt:coil-compose:2.6.0")

        /* ================= LOCATION ================= */
        implementation("com.google.android.gms:play-services-location:21.3.0")

        /* ================= TEST ================= */
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    }
