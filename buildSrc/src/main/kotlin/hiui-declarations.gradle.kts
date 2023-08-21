import driveindex.hiui.generateCoreDeclarations

tasks {
    named<Delete>("clean") {
        delete("src")
    }

    val generateHiuiDeclarations by registering {
        group = "hiui-gen"

        dependsOn(":kotlinNpmInstall")

        doLast {
            val typesDir = rootProject.buildDir
                .resolve("js/node_modules/@hiui")
            val sourceDir = projectDir.resolve("src/jsMain/kotlin")

            delete(sourceDir)

//            generateCoreDeclarations(
//                typesDir = typesDir,
//                sourceDir = sourceDir,
//            )
        }
    }

    kotlin.sequences.sequenceOf(
        "compileKotlinJs",
        "compileKotlinJsLegacy",
        "compileKotlinJsIr",
    ).mapNotNull(::findByName)
        .forEach { it.dependsOn(generateHiuiDeclarations) }
}
