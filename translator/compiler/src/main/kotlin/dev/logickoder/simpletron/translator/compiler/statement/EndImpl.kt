package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.Halt
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.End
import dev.logickoder.simpletron.translator.symbol.Symbol

internal class EndImpl(lineNumber: Int) : End(lineNumber) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            add(table.format(Halt, 0))
        }
    }
}