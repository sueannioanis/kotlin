// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_VARIABLE -UNUSED_PARAMETER -TOPLEVEL_TYPEALIASES_ONLY

fun outer() {
    <!UNSUPPORTED!><!WRONG_MODIFIER_TARGET!>companion<!> typealias TestLocal = Any<!>
}
