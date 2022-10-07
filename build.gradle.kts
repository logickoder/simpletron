import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.7.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

// instructions for all projects
allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "java")

    group = "dev.logickoder"
    version = "1.0-SNAPSHOT"

    val bundle by configurations.creating {
        extendsFrom(configurations["implementation"])
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    repositories {
        mavenCentral()
    }

//    testClasses.dependsOn publishToMavenLocal
}

// instructions for each subproject
subprojects {

    // common dependencies
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        // For Unit Testing
        testImplementation("junit:junit:4.13.2")
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    }

    // jar containing subproject source
    val sourcesJar by tasks.registering(Jar::class) {
        classifier = "sources"
        from(project.sourceSets["main"].allSource) // error here
    }

    tasks.jar {
        dependsOn(sourcesJar)
    }

    configure<PublishingExtension> {
        publications {
            register("mavenJava", MavenPublication::class) {
                groupId = "$group.${project.name}"
                artifactId = "simpletron-${project.name}"
                artifact(tasks.jar.get())
                artifact(sourcesJar.get())
            }
        }
    }
}

tasks.jar {
    isEnabled = false
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}