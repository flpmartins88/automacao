rootProject.name = "automacao"

include("tag", "item", "production", "inventory", "commons", "auth")

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        maven(url = "https://dl.bintray.com/gradle/gradle-plugins")
    }
}