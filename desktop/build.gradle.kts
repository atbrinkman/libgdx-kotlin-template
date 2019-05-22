plugins {
    kotlin("jvm")
}

val assetsDir = file("../android/assets")
val mainClassName = "com.example.desktop.DesktopLauncher"

sourceSets {
    main {
        java.srcDir("src/main/kotlin")
    }
}

dependencies {
    val gdxVersion: String by project
    
    implementation(project(":core"))

    implementation(kotlin("stdlib"))
    
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_6
    targetCompatibility = JavaVersion.VERSION_1_6
}

// Use this task to run the game if IntelliJ run application configuration doesn't work.
tasks.register<JavaExec>("run") {
    dependsOn(tasks.classes)
    main = mainClassName
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
}

// Use this task to create a fat jar.
tasks.register<Jar>("dist") {
    from(files(sourceSets.main.get().output.classesDirs))
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    from(assetsDir)

    manifest {
        attributes["Main-Class"] = mainClassName
    }
}