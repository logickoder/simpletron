package com.logickoder.simpletron.compiler.translator.utils

import com.logickoder.simpletron.compiler.translator.syntax.SyntaxError

/**
 * Converts a string to a syntax error with a simple string appended to it
 * */
fun String.toSyntaxError(lineNumber: Int): SyntaxError {
    return SyntaxError("Syntax Error on line $lineNumber, ${this.trim()}")
}