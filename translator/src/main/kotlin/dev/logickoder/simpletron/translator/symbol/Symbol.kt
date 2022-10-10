package dev.logickoder.simpletron.translator.symbol

import dev.logickoder.simpletron.translator.Subroutine
import dev.logickoder.simpletron.translator.syntax.SyntaxError

/**
 * Represents symbols used by the translator during code translation
 */
sealed interface Symbol {
    /**
     * The number at the start of every line of code in the program
     * */
    data class LineNumber(val lineNumber: Int, val subroutine: Subroutine) : Symbol

    /**
     * A variable that stores values at runtime
     *
     * @property name the name of the variable
     */
    data class Variable(val name: String, val subroutine: Subroutine) : Symbol {
        init {
            if (name.any { it.isDigit() })
                throw SyntaxError("\"$name\" is not a valid variable name")
        }
    }

    /**
     * A constant value set at compile time
     */
    data class Constant(val value: Float) : Symbol
}