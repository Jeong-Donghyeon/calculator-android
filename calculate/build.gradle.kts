plugins {
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.jlleitschuhGradleKtlint)
    alias(libs.plugins.gitlabArturboschDetekt)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$rootDir/detekt-config.yml"))
}

dependencies {
    testImplementation(libs.junit)
    implementation(group = "javax.inject", name = "javax.inject", version = "1")
}
