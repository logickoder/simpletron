plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm").version("1.7.20")
}

// instructions for all projects
allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "java")

    extra.apply {
        set("group_name", "dev.logickoder")
        set("version_name", "1.0-SNAPSHOT")
        val projectInfo = rootProject.name.split("-")
        set("project_name", projectInfo[0])
        set("project_module", projectInfo[1])
    }

    val bundle by configurations.creating {
        extendsFrom(configurations["implementation"])
    }

    group = extra["group_name"]
    version = extra["version_name"]

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    repositories {
        mavenCentral()
    }

//    testClasses.dependsOn publishToMavenLocal
}

// instructions for each sub project
subprojects {

    // common dependencies
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
        // For Unit Testing
        testImplementation("junit:junit:4.13.2")
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    }

    // jar containing subproject source
    val sourcesJar by tasks.registering(Jar::class) {
        classifier = "sources"
        from(project.sourceSets["main"].allSource) // error here
    }
    val jar = tasks.named<Jar>("jar")

    tasks.jar {
        dependsOn(sourcesJar)
    }

    configure<PublishingExtension> {
        publications {
            register("mavenJava", MavenPublication::class) {
                groupId = "${extra["group_name"]}.${extra["project_name"]}"
                artifactId = "${extra["project_module"]}-${project.name}"
                artifact(tasks.jar.get())
                artifact(sourcesJar.get())
            }
        }
    }
}

tasks.jar {
    isEnabled = false
}