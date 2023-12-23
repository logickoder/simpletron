// holds the list of projects that are to be also included in this module's jar file
val bundledProjects = arrayOf(
    project(":core:common"),
    project(":core:cpu"),
    project(":core:display"),
    project(":core:input"),
    project(":core:memory"),
)

dependencies {
    // add the projects as dependencies
    bundledProjects.forEach {
        api(it)
    }
}

tasks.apply {
    // single jar containing all the bundled projects compiled code
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        val bundledJars = bundledProjects.map {
            zipTree(File("${it.layout.buildDirectory}\\libs\\${it.name}-${it.version}.jar"))
        }.toTypedArray()

        from (*bundledJars)
    }

    // single jar containing all bundled project sources
    val sources = named("sources", Jar::class) {
        dependsOn.addAll(
            bundledProjects.map {
                it.tasks.named("sources").get()
            }
        )

        val bundledSources = bundledProjects.map {
            zipTree(File("${it.layout.buildDirectory}\\libs\\${it.name}-${it.version}-sources.jar"))
        }.toTypedArray()

        from(*bundledSources)
    }

    artifacts {
        archives(jar)
        archives(sources)
    }
}

// change the artifact id of the jar produced by this module to the module of this entire project
configure<PublishingExtension> {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "dev.logickoder.simpletron"
            artifactId = "core"
            artifact(tasks.jar.get())
            artifact(tasks.named("sources").get())
        }
    }
}