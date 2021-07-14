plugins {
    kotlin("jvm")           version "1.5.10" apply false
    kotlin("plugin.spring") version "1.5.10" apply false
    kotlin("plugin.jpa")    version "1.5.10" apply false

    id("org.springframework.boot")        version "2.5.1"  apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

group = "automation"
version = "1.0.0"

extra["springCloudVersion"] = "2020.0.3"

allprojects {
    repositories {
        mavenCentral()
        maven(url="https://packages.confluent.io/maven/")
        maven(url="https://repo.spring.io/milestone")
    }
}

subprojects {
    // TODO: Descobrir como configurar o java e o kotlin aqui
    // O padrão não é possível por que o plugin não foi aplicado nesse arquivo
}

//class DockerPlugin : Plugin<Project> {
//    override fun apply(target: Project) { decide gastar dinhei
////        project.task("createDockerFile") {
////            doFirst {
////                println("Generating DockerFile for projects")
////            }
////        }
//
//        project.task("buildDocker") {
//            doFirst {
//                println("Building docker images")
//            }
//        }
//    }
//}
//
//apply<DockerPlugin>()
