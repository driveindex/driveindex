@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kfc.library)

    `hiui-core-declarations`
}

dependencies {
    jsMainImplementation(npmv("@hi-ui/core"))

    jsMainImplementation(enforcedPlatform(libs.kotlinw.wrappers.bom))
}