package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.Read
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.Input
import dev.logickoder.simpletron.translator.symbol.Symbol

internal class InputImpl(lineNumber: Int, variables: String) : Input(lineNumber, variables) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            for (variable in variables) {
                add(table.format(Read, table.add(Symbol.Variable(variable, subroutine))))
            }
        }
    }
}