// holds the list of projects that are to be also included in this module's jar file
val bundledProjects = arrayOf(
    project(":core:component"),
    project(":core:contract"),
    project(":core:register"),
    project(":core:instruction"),
    project(":core:utils"),
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
            val jarFile = File ("${it.buildDir}\\libs\\${it.name}-${it.version}.jar")
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
the<PublishingExtension>().apply {
    publications {
        maybeCreate("mavenJava", MavenPublication::class).apply {
            artifactId = "simpletron-core"
        }
    }
}