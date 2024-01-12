plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$rootDir/detekt-config.yml"))
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation(group = "javax.inject", name = "javax.inject", version = "1")
}
