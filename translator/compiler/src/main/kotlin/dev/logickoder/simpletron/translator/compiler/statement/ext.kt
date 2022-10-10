package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.translator.compiler.Location
import dev.logickoder.simpletron.translator.compiler.SymbolTable.Companion.DOES_NOT_EXIST
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.syntax.syntaxError

internal fun Symbol.check(config: CompilerConfig, lineNumber: Location) = when (this) {
    is Symbol.Constant -> config.table.add(this)
    is Symbol.Variable -> config.table[this].also { location ->
        if (location == DOES_NOT_EXIST) {
            throw "This variable \"${this.name}\", has not been defined".syntaxError(lineNumber, true)
        }
    }

    else -> throw AssertionError("Only variables or constants are valid for this check")
}

