import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.72"
    id("com.commercehub.gradle.plugin.avro") version "0.21.0"
}

java.sourceCompatibility = JavaVersion.VERSION_13

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.avro:avro:1.10.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "13"
    }
}

avro {
    setCreateSetters(false)
    setFieldVisibility("private")
}

sourceSets {
    main {
        java.srcDir("build/generated-main-avro-java")
    }
}

val instrumentedJars by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, namedAttribute(Category.LIBRARY))
        attribute(Usage.USAGE_ATTRIBUTE, namedAttribute(Usage.JAVA_RUNTIME))
        attribute(Bundling.BUNDLING_ATTRIBUTE, namedAttribute(Bundling.EXTERNAL))
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, JavaVersion.current().majorVersion.toInt())
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, namedAttribute("instrumented-jar"))
    }
}

inline fun <reified T: Named> Project.namedAttribute(value: String) = objects.named(T::class.java, value)