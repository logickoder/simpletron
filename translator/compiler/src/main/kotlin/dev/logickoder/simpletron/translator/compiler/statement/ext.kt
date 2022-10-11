package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.translator.compiler.symbol.Location
import dev.logickoder.simpletron.translator.compiler.symbol.SymbolTable.Companion.DOES_NOT_EXIST
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.syntax.syntaxError

internal fun Symbol.check(config: CompilerConfig, lineNumber: Location) = when (this) {
    // Don't initialize constants as that should be done after the compilation of the program
    is Symbol.Constant -> 0
    is Symbol.Variable -> config.table[this].also { location ->
        if (location == DOES_NOT_EXIST) {
            throw "This variable \"${this.name}\", has not been defined".syntaxError(lineNumber, true)
        }
    }

    else -> throw AssertionError("Only variables or constants are valid for this check")
}

