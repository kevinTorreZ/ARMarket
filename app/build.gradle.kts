plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.armarket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.armarket"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth)

    // Material Design
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation(libs.material3)
    // Navigation Components
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Legacy Support (si es estrictamente necesario)
    implementation(libs.androidx.legacy.support.v4) // Obsoleto, intenta evitar su uso.
    implementation(libs.androidx.legacy.support.core.utils) // Reemplaza si es posible.

    // Lifecycle y ViewModel (remplazo de lifecycle-extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx) // Versión corregida sin el sufijo `.v261`

    // Anotaciones del ciclo de vida
    implementation(libs.androidx.lifecycle.compiler)

    // Core libraries
    implementation(libs.androidx.core.ktx)

    // Jetpack Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Play Services
    implementation(libs.credentials)
    implementation(libs.credentialsPlayServicesAuth)
    implementation(libs.google.googleid)

    implementation(libs.play.services.auth)
    // Retrofit y GSON para la red
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Facebook SDK
    implementation(libs.facebook.android.sdk)
    implementation(libs.identity.credential)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.livedata.ktx) // Verifica la última versión
    implementation(libs.androidx.runtime.livedata) // Verifica la última versión
}
