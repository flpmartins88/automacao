plugins {
    kotlin("jvm")           version "1.6.10" apply false
    kotlin("plugin.spring") version "1.6.10" apply false
    kotlin("plugin.jpa")    version "1.6.10" apply false

    id("org.springframework.boot")        version "2.6.3"  apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

allprojects {
    group = "automation"
    version = "1.0.0"

    extra["springCloudVersion"] = "2021.0.0"

    repositories {
        mavenCentral()
        maven(url="https://packages.confluent.io/maven/")
        maven(url="https://repo.spring.io/milestone")
    }


}

subprojects {

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

//    tasks.getByName<BootJar>("bootJar") {
//        archiveClassifier.set("boot")
//    }

//    tasks.getByName<Jar>("jar") {
//        archiveClassifier.set("")
//    }
    // TODO: Descobrir como configurar o java e o kotlin aqui
    // O padrão não é possível por que o plugin não foi aplicado nesse arquivo
}

//class DockerPlugin : Plugin<Project> {
//    override fun apply(target: Project) {
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
