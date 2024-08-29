// DONT_TARGET_EXACT_BACKEND: JS
// ES_MODULES
// SPLIT_PER_MODULE
// MODULE: lib
// FILE: lib.kt
package lib

fun bar(f: () -> String) = f()

inline fun foo(): String {
    return bar { "OK" }
}

// MODULE: main(lib)
// FILE: main.kt
package main

import lib.*

fun box() = foo()
