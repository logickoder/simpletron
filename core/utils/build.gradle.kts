// holds the list of projects that are to be also included in this module's jar file
val bundledProjects = arrayOf(project(":component"))

dependencies {
    // add the projects as dependencies
    bundledProjects.forEach {
        implementation(it)
    }

    implementation("org.reflections:reflections:0.10.2")
    testImplementation(project(":impl"))
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