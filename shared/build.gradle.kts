import java.util.Properties
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
}

@CacheableTask
abstract class GenerateAppConfigTask : DefaultTask() {
    @get:Input
    abstract val supabaseUrl: Property<String>

    @get:Input
    abstract val supabaseKey: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val packageDir = outputDir.get().asFile.resolve("com/acs/oboeruapp/core/config")
        packageDir.mkdirs()

        val url = supabaseUrl.get().escapeForKotlinString()
        val key = supabaseKey.get().escapeForKotlinString()

        packageDir.resolve("AppConfig.kt").writeText(
            """
            package com.acs.oboeruapp.core.config

            internal object AppConfig {
                const val SUPABASE_URL: String = "$url"
                const val SUPABASE_KEY: String = "$key"
            }
            """.trimIndent() + "\n"
        )
    }

    private fun String.escapeForKotlinString(): String =
        replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("$", "\\$")
}

val localPropertiesText: Provider<String> = providers.fileContents(
    rootProject.layout.projectDirectory.file("local.properties")
).asText.orElse("")

fun resolveConfigValue(key: String): Provider<String> =
    localPropertiesText.zip(providers.environmentVariable(key).orElse("")) { text, envValue ->
        val fromFile = if (text.isBlank()) {
            ""
        } else {
            Properties().apply { load(text.reader()) }
                .getProperty(key)
                .orEmpty()
                .trim()
                .removeSurrounding("\"")
        }
        fromFile.ifEmpty { envValue.trim().removeSurrounding("\"") }
    }

val generateAppConfig = tasks.register<GenerateAppConfigTask>("generateAppConfig") {
    supabaseUrl.set(resolveConfigValue("SUPABASE_URL"))
    supabaseKey.set(resolveConfigValue("SUPABASE_KEY"))
    outputDir.set(layout.buildDirectory.dir("generated/source/appConfig/commonMain/kotlin"))
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    android {
       namespace = "com.acs.oboeruapp.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
        commonMain {
            kotlin.srcDir(generateAppConfig.flatMap { it.outputDir })
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.compose.navigation)
                implementation(libs.ktor.client.core)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
                implementation(libs.supabase.postgrest)
                implementation(libs.supabase.auth)
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.okio)
                implementation(libs.sqldelight.coroutines)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

sqldelight {
    databases {
        create("OboeruDatabase") {
            packageName.set("com.acs.oboeruapp.db")
        }
    }
    linkSqlite.set(true)
}