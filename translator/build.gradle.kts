dependencies {
    implementation("org.reflections:reflections:0.10.2")
    implementation(project(":core"))
    api(project(":translator:infix-postfix"))
}