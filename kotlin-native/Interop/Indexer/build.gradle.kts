/*
 * Copyright 2010-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jetbrains.kotlin.konan.target.*

plugins {
    kotlin("jvm")
    id("native-dependencies")
}

val nativeLibsForTest by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, ArtifactTypeDefinition.DIRECTORY_TYPE)
        attribute(TargetWithSanitizer.TARGET_ATTRIBUTE, TargetWithSanitizer.host)
    }
}

dependencies {
    api(project(":kotlin-stdlib"))
    api(project(":kotlin-native:Interop:Runtime"))
    api(project(":kotlin-native:libclangInterop"))
    implementation(project(":native:kotlin-native-utils"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":compiler:util"))
    nativeLibsForTest(project(":kotlin-native:libclangInterop", "nativeLibs"))
    nativeLibsForTest(project(":kotlin-native:Interop:Runtime", "nativeLibs"))
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        optIn.addAll(
                listOf(
                        "kotlinx.cinterop.BetaInteropApi",
                        "kotlinx.cinterop.ExperimentalForeignApi",
                )
        )
        freeCompilerArgs.addAll(
                listOf(
                        "-Xskip-prerelease-check",
                        // staticCFunction uses kotlin.reflect.jvm.reflect on its lambda parameter.
                        "-Xlambdas=class",
                )
        )
    }
}

open class TestArgumentProvider @Inject constructor(
        objectFactory: ObjectFactory,
) : CommandLineArgumentProvider {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    val nativeLibraries: ConfigurableFileCollection = objectFactory.fileCollection()

    override fun asArguments(): Iterable<String> = listOf(
            "-Djava.library.path=${nativeLibraries.files.joinToString(File.pathSeparator) { it.parentFile.absolutePath }}"
    )
}

tasks.withType<Test>().configureEach {
    dependsOn(nativeDependencies.llvmDependency)
    jvmArgumentProviders.add(objects.newInstance<TestArgumentProvider>().apply {
        nativeLibraries.from(nativeLibsForTest)
    })

    systemProperty("kotlin.native.llvm.libclang", "${nativeDependencies.llvmPath}/" + if (HostManager.hostIsMingw) {
        "bin/libclang.dll"
    } else {
        "lib/${System.mapLibraryName("clang")}"
    })

    systemProperty("kotlin.native.interop.indexer.temp", layout.buildDirectory.dir("testTemp").get().asFile)
}

