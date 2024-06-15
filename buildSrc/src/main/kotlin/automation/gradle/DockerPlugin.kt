package automation.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class DockerPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.task("dockerfile") {
            doLast {
                println("Creating dockerfile")
                val folder = project.layout.buildDirectory.get().asFile
                File(folder, "Dockerfile").writeText("""
                    FROM amazoncorretto:22-alpine

                    COPY libs/${project.name}.jar /automation/app.jar
                    ENTRYPOINT ["java","-jar","/automation/app.jar"]
                """.trimIndent())
            }
        }.dependsOn("build")

        project.task("docker-image") {
            doLast {
                val workingDir = project.layout.buildDirectory.get().asFile
                val tag = "${project.name}-service:${project.version}"
                println("Building '$tag' image")
                project.exec {
                    commandLine("docker", "buildx", "build", workingDir, "-t", tag)
                }
            }
        }.dependsOn("dockerfile")

        project.task("docker-tag") {
            doLast {
                val tag = "${project.name}-service:${project.version}"
                println("Tagging '$tag' image as latest")
                project.exec {
                    commandLine("docker", "tag", "${project.name}-service:${project.version}", "${project.name}-service:latest")
                }
            }
        }.dependsOn("docker-image")

        project.task("docker") {

        }.dependsOn("docker-image", "docker-tag")
    }
}
