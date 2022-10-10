package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.translator.Subroutine
import dev.logickoder.simpletron.translator.compiler.SymbolTable
import dev.logickoder.simpletron.translator.config.Config

/**
 * Configuration used during the compilation of statements
 *
 * @property table used to store symbols during compilation
 */
internal data class CompilerConfig(
    val table: SymbolTable,
    val subroutine: Subroutine,
    val subroutines: List<Subroutine>
) : Config