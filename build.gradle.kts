plugins {
    application
    kotlin("jvm") version "2.1.10"
    kotlin("kapt") version "2.2.20"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("krilovs.andrejs.app.Main")
}

kotlin {
    jvmToolchain(21)
    charset("UTF-8")
}

dependencies {
    implementation(kotlin("stdlib"))
    kapt("org.mapstruct:mapstruct-processor:1.6.3")
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("org.eclipse.jetty:jetty-server:11.0.26")
    implementation("org.eclipse.jetty:jetty-servlet:11.0.26")
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.0")
}