import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
}

group = "automacao"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_16

repositories {
	mavenCentral()
}

dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	// Esse precisa da versão por causa de compatibilidade até ser migrado de vez para dentro do spring security
	implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.5.2")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

//	implementation("org.springframework.cloud:spring-cloud-starter-oauth2")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.springframework.security:spring-security-test")

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
		jvmTarget = "16"
	}
}
