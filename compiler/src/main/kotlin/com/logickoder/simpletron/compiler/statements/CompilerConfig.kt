package com.logickoder.simpletron.compiler.statements

import com.logickoder.simpletron.compiler.SymbolTable
import com.logickoder.simpletron.translator.Subroutine
import com.logickoder.simpletron.translator.config.Config

/**
 * Configuration used during the compilation of statements
 *
 * @property table used to store symbols during compilation
 */
data class CompilerConfig(
    val table: SymbolTable,
    val subroutine: Subroutine,
    val subroutines: List<Subroutine>
) : Config