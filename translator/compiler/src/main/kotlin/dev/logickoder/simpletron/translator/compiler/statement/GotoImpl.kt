package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.Branch
import dev.logickoder.simpletron.translator.compiler.symbol.SymbolTable
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.Goto
import dev.logickoder.simpletron.translator.symbol.Symbol

internal class GotoImpl(lineNumber: Int, expression: String) : Goto(lineNumber, expression) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            val lineNum = Symbol.LineNumber(line, subroutine)
            val location = table[lineNum].let {
                if (it == SymbolTable.DOES_NOT_EXIST) {
                    table.flag(lineNum)
                    0
                } else it
            }
            add(table.format(Branch, location))
        }
    }
}