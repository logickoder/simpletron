package com.logickoder.simpletron.translator.symbol


/**
 * Resolves a string encountered during translation to a symbol
 */
typealias SymbolResolver = (String) -> Symbol

/**
 * Represents symbols used by the translator during code translation
 *
 * @property location the location of the symbol in the memory during runtime
 */
sealed class Symbol(val location: Int) {
    /**
     * The number at the start of every line of code in the program
     * */
    class LineNumber(lineNumber: Int, location: Int) : Symbol(location) {
        companion object {
            fun Int.toLineNumber() = "${this}L"
            fun String.toLineNumber() = "${this}L"
        }
    }

    /**
     * A variable that stores values at runtime
     *
     * @property name the name of the variable
     */
    class Variable(val name: String, location: Int) : Symbol(location)

    /**
     * A constant value set at compile time
     */
    class Constant(value: Float, location: Int) : Symbol(location)
}

