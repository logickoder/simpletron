package com.logickoder.simpletron.translator.symbol

import com.logickoder.simpletron.translator.syntax.SyntaxError

/**
 * Represents symbols used by the translator during code translation
 */
sealed class Symbol {
    /**
     * The number at the start of every line of code in the program
     * */
    data class LineNumber(val lineNumber: Int) : Symbol()

    /**
     * A variable that stores values at runtime
     *
     * @property name the name of the variable
     */
    data class Variable(val name: String) : Symbol() {
        init {
            if (name.any { it.isDigit() })
                throw SyntaxError("\"$this\" is not a valid variable name")
        }
    }

    /**
     * A constant value set at compile time
     */
    data class Constant(val value: Float) : Symbol()
}

/**
 * Creates either a [Symbol.Constant] or [Symbol.Variable] from the given string
 */
fun String.toSymbol() = when {
    toFloatOrNull() != null -> Symbol.Constant(toFloat())
    else -> Symbol.Variable(this)
}