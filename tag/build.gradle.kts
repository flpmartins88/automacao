import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
}

group = "automation"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_13

repositories {
    mavenCentral()
    maven(url="https://packages.confluent.io/maven/")
}

dependencies {

    implementation(project(":commons"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java")
//    TODO: Ver para usar o R2DBC com o Spring Data
//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//    implementation("org.springframework.data:spring-data-r2dbc:1.1.3.RELEASE")
//    implementation("dev.miku:r2dbc-mysql:0.8.2.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springframework.kafka:spring-kafka")
    implementation(group = "io.confluent", name = "kafka-avro-serializer", version = "5.5.1") {
        exclude(group = "org.slf4j")
        exclude(group = "log4j")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("com.h2database:h2")
}

extra["springCloudVersion"] = "Hoxton.SR4"

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
        jvmTarget = "13"
    }
}
