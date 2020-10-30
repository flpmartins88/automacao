plugins {
    kotlin("jvm")           version "1.4.10" apply false
    kotlin("plugin.spring") version "1.4.10" apply false
    kotlin("plugin.jpa")    version "1.4.10" apply false

    id("org.springframework.boot")        version "2.3.4.RELEASE"  apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false

    id("com.commercehub.gradle.plugin.avro") version "0.21.0" apply false
}

group = "automation"
version = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
        maven(url="https://packages.confluent.io/maven/")
    }
}

subprojects {
    // TODO: Descobrir como configurar o java e o kotlin aqui
    // O padrão não é possível por que o plugin não foi aplicado nesse arquivo
}