rootProject.name = "automation"

include("tag", "item", "production", "inventory", "commons", "auth", "order", "gateway")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo1.maven.org/maven2/")
        maven(url = "https://dl.bintray.com/gradle/gradle-plugins")
    }
}
include("data-stream-processor")
include("integration")
