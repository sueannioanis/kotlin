/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:kotlin.internal.JvmBuiltin
@file:kotlin.internal.SuppressBytecodeGeneration

package kotlin.coroutines

/**
 * This is necessary to force generation of coroutines.kotlin_builtins file, thus providing builtin package fragment for kotlin.coroutines
 * package. This way we can use kotlin.coroutines.SuspendFunction{N} interfaces in code.
 */
private fun hackToForceKotlinBuiltinsForKotlinCoroutinesPackage() {}
