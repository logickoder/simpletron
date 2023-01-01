package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.Read
import dev.logickoder.simpletron.translator.compiler.CompileError
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.Input
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.symbol.toSymbol

internal class InputImpl(lineNumber: Int, variables: String) : Input(lineNumber, variables) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            for (variable in variables) {
                val symbol = variable.toSymbol(subroutine)
                if (symbol !is Symbol.Variable)
                    throw CompileError("Only variables can be used in input statements")
                add(table.format(Read, table.add(symbol)))
            }
        }
    }
}