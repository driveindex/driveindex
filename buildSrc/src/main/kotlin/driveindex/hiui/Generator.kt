package driveindex.hiui

import java.io.File

/**
 * @author Madray Haven
 * @Date 2023/8/21 14:00
 */
private const val GENERATOR_COMMENT = "Automatically generated - do not modify!"

private val DEFAULT_IMPORTS = listOf<Pair<String, String>>(

)

// language=kotlin
private val TYPES_PROPS_WITH_COMPONENT = """
external interface PropsWithComponent : react.Props {
    var component: react.ElementType<*>?
}
""".trimIndent()

fun generateCoreDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    generateTypesDeclarations(sourceDir)
}

private fun generateTypesDeclarations(
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("hiui/types")
        .also { it.mkdirs() }

    targetDir.resolve("PropsWithComponent.kt")
        .writeText(fileContent(body = TYPES_PROPS_WITH_COMPONENT, pkg = Package.types))
}


private fun fileContent(
    annotations: String = "",
    body: String,
    pkg: Package,
): String {
    val defaultImports = DEFAULT_IMPORTS
        .filter { it.first in body }
        .map { it.second }
        .plus(systemImports(body, pkg))
        .map { "import $it" }
        .joinToString("\n")

    return sequenceOf(
        "// $GENERATOR_COMMENT",
        annotations,
        "package ${pkg.pkg}",
        defaultImports,
        body,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")
        .removeSuffix("\n") + "\n"
}

private enum class Package(
    id: String? = null,
    pkg: String? = null,
) {
    types,
    base,
    material,
    materialStyles("material/styles"),
    materialTransitions,
    iconsMaterial("icons-material"),
    system,
    pickers("x-date-pickers", "muix.pickers"),
    lab,

    dateioCore("", "dateio.core"),

    ;

    val id = id ?: name

    val pkg: String = pkg ?: ("mui." + name.replace(Regex("""[A-Z]""")) {
        @Suppress("DEPRECATION")
        "." + it.value.toLowerCase()
    })
}

private fun systemImports(
    body: String,
    pkg: Package,
): Sequence<String> =
    if ("SxProps<Theme>" in body && pkg != Package.system) {
        sequenceOf(
            "mui.material.styles.Theme",
            "mui.system.SxProps",
        )
    } else emptySequence()
