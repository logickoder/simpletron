package com.logickoder.simpletron.translator.syntax

/**
 *
 * A message containing what is wrong with a line of code in simpletron basic language
 *
 * @property message what this error is all about
 */
class SyntaxError(message: String) : RuntimeException(message) {
    companion object {
        const val UNKNOWN_ERROR = "unknown error occurred"
    }
}

/**
 * Converts a string to a syntax error with a simple string appended to it
 * */
fun String.syntaxError() = SyntaxError("Syntax Error, ${this.trim()}")

/**
 * Converts a string to a syntax error with a simple string appended to it
 *
 * @param index the line number of the statement
 * @param isLineNumber does the [index] refer to the program line number
 * */
fun String.syntaxError(index: Int, isLineNumber: Boolean = false): SyntaxError {
    return SyntaxError(
        "Syntax Error on line ${
            if (isLineNumber) "number $index" else "${index + 1}"
        }, ${this.trim()}"
    )
}