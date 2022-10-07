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
        implementation(it)
    }
}

// single jar containing all the bundled projects compiled code
tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from({
        bundledProjects.map {
            val jarFile = File("${it.buildDir}\\libs\\${it.name}-${it.version}.jar")
            zipTree(jarFile)
        }
    })
}

// single jar containing all bundled project sources
tasks.sourcesJar {
    classifier = "sources"
    bundledProjects.forEach {
        from(it.sourceSets["main"].allSource)
    } // error here
}

// change the artifact id of the jar produced by this module to the module of this entire project
configure<PublishingExtension> {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "dev.logickoder.simpletron"
            artifactId = "core"
            artifact(tasks.jar.get())
            artifact(tasks.sourcesJar.get())
        }
    }
}