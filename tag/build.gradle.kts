import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") 
    id("io.spring.dependency-management") 
    kotlin("jvm") 
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {

    implementation(project(":commons"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    runtimeOnly("mysql:mysql-connector-java")

    implementation("org.springframework.kafka:spring-kafka")
    implementation(group = "io.confluent", name = "kafka-avro-serializer", version = "7.0.1") {
        exclude(group = "org.slf4j")
        exclude(group = "log4j")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    testRuntimeOnly("com.h2database:h2")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
