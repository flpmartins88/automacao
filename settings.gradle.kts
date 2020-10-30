rootProject.name = "automacao"

include("tag", "item", "production", "stock", "commons", "auth")

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        maven(url = "https://dl.bintray.com/gradle/gradle-plugins")
    }
}