package automation.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.dependencies

class SpringKafkaPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.dependencies {
            this.add("implementation", "org.springframework.kafka:spring-kafka")
            this.add("testImplementation", "org.springframework.kafka:spring-kafka-test")
        }
    }
}
