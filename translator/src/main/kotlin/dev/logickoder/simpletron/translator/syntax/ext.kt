package dev.logickoder.simpletron.translator.syntax


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