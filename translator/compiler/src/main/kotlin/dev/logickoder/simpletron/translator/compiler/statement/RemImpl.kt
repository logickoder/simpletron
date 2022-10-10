package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.Rem
import dev.logickoder.simpletron.translator.symbol.Symbol

internal class RemImpl(lineNumber: Int, comment: String) : Rem(lineNumber, comment) {
    override fun <T : Config> translate(config: T) = buildList<Int> {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
        }
    }
}