plugins {
    id("com.android.library")
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.googleDaggerHiltAndroid)
    alias(libs.plugins.jlleitschuhGradleKtlint)
    alias(libs.plugins.gitlabArturboschDetekt)
}

android {

    namespace = "com.donghyeon.dev.calculator.data"
    compileSdk = 35

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.gson)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
}
