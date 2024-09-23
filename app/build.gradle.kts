plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.weartile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weartile"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(libs.core.splashscreen)
    implementation(libs.tiles)
    implementation(libs.proto)
    implementation(libs.proto.material)
    implementation(libs.proto.expression)
    implementation(libs.watchface.complications.data.source.ktx)
    debugImplementation(libs.tiles.tooling)
    implementation(libs.tiles.tooling.preview)
    implementation (libs.guava)
}