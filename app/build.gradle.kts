import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.googleDaggerHiltAndroid)
    alias(libs.plugins.jlleitschuhGradleKtlint)
    alias(libs.plugins.gitlabArturboschDetekt)
    id("com.google.android.gms.oss-licenses-plugin")
}

android {

    namespace = "com.donghyeon.dev.calculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.donghyeon.dev.calculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val keystoreProperties =
        Properties().apply {
            load(FileInputStream(rootProject.file("keystore.properties")))
        }

    signingConfigs {
        create("release") {
            storeFile = file(rootProject.file("calculator.jks"))
            keyAlias = keystoreProperties["keyAlias"].toString()
            keyPassword = keystoreProperties["keyPassword"].toString()
            storePassword = keystoreProperties["storePassword"].toString()
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = "18"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$rootDir/detekt-config.yml"))
}

dependencies {
    implementation(project(":data"))
    implementation(project(":calculate"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.play.services.oss.licenses)
    implementation(libs.bottomsheetdialog.compose)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
