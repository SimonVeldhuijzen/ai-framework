plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.20")
    application
}

repositories {
    mavenCentral()
}

val slf4jVersion = "1.7.26"
val jacksonVersion = "2.9.8"
val javalinVersion = "2.8.0"
val coroutinesVersion = "1.1.1"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    compile("io.javalin", "javalin", javalinVersion)
    compile("org.slf4j", "slf4j-api", slf4jVersion)
    compile("org.slf4j", "slf4j-log4j12", slf4jVersion)
    compile("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    compile("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    compile("org.jetbrains.kotlinx", "kotlinx-coroutines-core", coroutinesVersion)
}

application {
    mainClassName = "ai.framework.AppKt"
}
