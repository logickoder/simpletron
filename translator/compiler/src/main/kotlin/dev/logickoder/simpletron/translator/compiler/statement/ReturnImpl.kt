package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.Return
import dev.logickoder.simpletron.translator.symbol.Symbol

internal class ReturnImpl(lineNumber: Int) : Return(lineNumber) {

    override fun <T : Config> translate(config: T) = buildList<Int> {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
        }
    }
}