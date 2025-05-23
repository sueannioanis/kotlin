/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.checkers

import org.jetbrains.kotlin.fir.analysis.cfa.FirReturnsImpliesAnalyzer
import org.jetbrains.kotlin.fir.analysis.checkers.cfa.FirControlFlowChecker
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirPropertyChecker
import org.jetbrains.kotlin.fir.analysis.checkers.experimental.RedundantExplicitTypeChecker

object ExperimentalDeclarationCheckers : DeclarationCheckers() {
    override val controlFlowAnalyserCheckers: Set<FirControlFlowChecker> = setOf(
        FirReturnsImpliesAnalyzer,
    )

    override val propertyCheckers: Set<FirPropertyChecker> = setOf(
        RedundantExplicitTypeChecker,
    )
}
