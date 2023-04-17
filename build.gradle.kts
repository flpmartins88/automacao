import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")           version "1.7.22" apply false
    kotlin("plugin.spring") version "1.7.22" apply false
    kotlin("plugin.jpa")    version "1.7.22" apply false

    id("org.springframework.boot") version "3.0.5" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
}

allprojects {
    group = "automation"
    version = "1.0.0"

    extra["springCloudVersion"] = "2022.0.2"

    repositories {
        mavenCentral()
        maven(url="https://packages.confluent.io/maven/")
        maven(url="https://repo.spring.io/milestone")
    }
}

subprojects {

    afterEvaluate {

        // https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/gradle-plugin/reference/html/#build-image
        //tasks.getByName<BootBuildImage>("bootBuildImage") {
        //    imageName = ""
        //}


//        configurations {
//            compileOnly {
//                extendsFrom(configurations.annotationProcessor.get())
//            }
//        }

        // Fix java and kotlin versions to all subprojects
        if (pluginManager.hasPlugin("java")) {
            configure<JavaPluginExtension> {
                this.sourceCompatibility = JavaVersion.VERSION_18
                this.targetCompatibility = JavaVersion.VERSION_18
            }
            tasks.withType<Test> {
                useJUnitPlatform()
            }
        }

        if (pluginManager.hasPlugin("kotlin")) {
            tasks.withType<KotlinCompile> {
                kotlinOptions {
                    freeCompilerArgs = listOf("-Xjsr305=strict")
                    jvmTarget = "18"
                }
            }
        }

        if (pluginManager.hasPlugin("org.springframework.boot")) {
            // Set jar name to all subprojects
            tasks.getByName<BootJar>("bootJar") {
                this.archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
            }

            // Configure dependency management to align spring boot and spring cloud dependency matrix
            configure<DependencyManagementExtension> {
                imports {
                    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
                }
            }
        }
    }

    //getAllTasks(true)
    //tasks.findByName("jar")?.enabled = false

//    tasks.named<Jar>("jar") {
//        enabled = false
//    }

//    getAllTasks(true)
//    tasks.named<BootJar>("bootJar") {
//        archiveClassifier.set("service")
//    }

//    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
//        imageName = "localhost:5000/${rootProject.group}/${project.name}-service:$version"
//        isPublish = true
//        docker.publishRegistry.url = "localhost:5000"
//        docker.publishRegistry.username = "admin"
//        docker.publishRegistry.password = "12345678"
//    }

//    val bootBuildImage = tasks.named<BootBuildImage>("bootBuildImage") {
//        imageName = "automacao/${project.name}"
////        buildpacks = listOf("file:///path/to/example-buildpack.tgz", "urn:cnb:builder:paketo-buildpacks/java")
//    }
//
//    bootBuildImage {
//        onlyIf { project.task("bootBuildImage") != null }
//    }

//    tasks.getByName<BootJar>("bootJar") {
//        archiveClassifier.set("boot")
//    }

//    tasks.getByName<Jar>("jar") {
//        archiveClassifier.set("")
//    }

}
