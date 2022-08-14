
plugins {
    base
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("docker-plugin") {
            id = "automation.docker"
            implementationClass = "automation.gradle.DockerPlugin"
        }
    }
}
