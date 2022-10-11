package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.BranchNeg
import dev.logickoder.simpletron.core.cpu.BranchZero
import dev.logickoder.simpletron.core.cpu.Load
import dev.logickoder.simpletron.core.cpu.Subtract
import dev.logickoder.simpletron.translator.compiler.symbol.SymbolTable
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.If
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.symbol.toSymbol

internal class IfImpl(lineNumber: Int, expression: String) : If(lineNumber, expression) {


    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            val x = symbols[0].toSymbol(subroutine)
            val y = symbols[1].toSymbol(subroutine)

            when (operator) {
                "==", "<", "<=" -> {
                    // load x from the memory
                    if (x is Symbol.Constant) table.flag(x)
                    add(table.format(Load, x.check(config, lineNumber)))
                    // subtract y from it
                    if (y is Symbol.Constant) table.flag(y)
                    add(table.format(Subtract, y.check(config, lineNumber)))
                }

                ">", ">=" -> {
                    // load y from the memory
                    if (y is Symbol.Constant) table.flag(y)
                    add(table.format(Load, y.check(config, lineNumber)))
                    // subtract x from it
                    if (x is Symbol.Constant) table.flag(x)
                    add(table.format(Subtract, x.check(config, lineNumber)))
                }
            }
            val lineNumber = Symbol.LineNumber(gotoLocation.toInt(), subroutine)
            val flag: Boolean
            val location = table[lineNumber].let {
                flag = it == SymbolTable.DOES_NOT_EXIST
                if (flag) {
                    table.flag(lineNumber)
                    0
                } else it
            }

            when (operator) {
                "==" -> {
                    add(table.format(BranchZero, location))
                }

                ">=", "<=" -> {
                    add(table.format(BranchZero, location))
                    if (flag) table.flag(lineNumber)
                    add(table.format(BranchNeg, location))
                }

                ">", "<" -> {
                    add(table.format(BranchNeg, location))
                }
            }
        }
    }
}