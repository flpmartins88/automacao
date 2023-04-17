plugins {
    id("org.springframework.boot") 
    id("io.spring.dependency-management") 
    kotlin("jvm") 
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("automation.docker")
}

dependencies {

    implementation(project(":commons"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // FIXME: Change to micrometer
//    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.springframework.kafka:spring-kafka")
    implementation(group = "io.confluent", name = "kafka-avro-serializer", version = "7.3.3") {
        exclude(group = "org.slf4j")
        exclude(group = "log4j")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    testRuntimeOnly("com.h2database:h2")

}
