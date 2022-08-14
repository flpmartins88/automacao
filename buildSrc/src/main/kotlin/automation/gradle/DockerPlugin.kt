package automation.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import java.io.File

class DockerPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.task("dockerfile") {
            doLast {
                println("Creating dockerfile")
                val folder = project.mkdir("${project.buildDir}/docker")
                File(folder, "Dockerfile").writeText("""
                    FROM openjdk:18-alpine

                    COPY build/libs/${project.name}.jar /automation/app.jar
                    ENTRYPOINT ["java","-jar","/automation/app.jar"]
                """.trimIndent())
            }
        }.dependsOn("build")

        project.task("docker") {
            doLast {
                println("Hello from the GreetingPlugin")
            }
        }.dependsOn("dockerfile")

    }

}
