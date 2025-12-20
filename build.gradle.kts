plugins {
    id("application")
    id("java")
    id("jvm-test-suite")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = "21.0.9"
    modules("javafx.controls", "javafx.fxml")
}

group = "at.ac.hcw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}