package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.NewLine
import dev.logickoder.simpletron.core.cpu.Write
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.Print
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.symbol.toSymbol

internal class PrintImpl(lineNumber: Int, symbols: String) : Print(lineNumber, symbols) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            symbols.forEach {
                val symbol = it.toSymbol(subroutine)
                if (symbol is Symbol.Constant) table.flag(symbol)
                val check = symbol.check(config, lineNumber)
                add(table.format(Write, if (check != -1) check else 0))
                add(table.format(NewLine, 0))
            }
        }
    }
}