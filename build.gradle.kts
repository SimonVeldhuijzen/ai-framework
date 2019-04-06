plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.20")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    compile("io.javalin", "javalin", "2.8.0")
    compile("org.slf4j", "slf4j-api", "1.7.26")
    compile("org.slf4j", "slf4j-log4j12", "1.7.26")
    compile("com.fasterxml.jackson.core", "jackson-databind", "2.9.8")
    compile("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.9.8")
}

application {
    mainClassName = "ai.framework.AppKt"
}
