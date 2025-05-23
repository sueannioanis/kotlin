/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.wasm.checkers.declaration

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.closestNonLocalWith
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirBasicDeclarationChecker
import org.jetbrains.kotlin.fir.analysis.checkers.isTopLevel
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.utils.isEffectivelyExternal
import org.jetbrains.kotlin.name.WebCommonStandardClassIds.Annotations.JsModule

object FirWasmJsModuleChecker : FirBasicDeclarationChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirDeclaration) {
        if (declaration is FirFile || !declaration.hasAnnotation(JsModule, context.session)) return

        if (declaration is FirProperty && declaration.isVar) {
            reporter.reportOn(declaration.source, FirWasmErrors.JS_MODULE_PROHIBITED_ON_VAR)
        }

        if (!declaration.symbol.isEffectivelyExternal(context.session)) {
            reporter.reportOn(declaration.source, FirWasmErrors.JS_MODULE_PROHIBITED_ON_NON_EXTERNAL)
        }

        if (context.isTopLevel && context.containingFile?.hasAnnotation(JsModule, context.session) == true) {
            reporter.reportOn(declaration.source, FirWasmErrors.NESTED_JS_MODULE_PROHIBITED)
        }
    }
}
