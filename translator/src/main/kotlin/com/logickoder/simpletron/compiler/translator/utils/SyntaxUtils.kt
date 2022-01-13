package com.logickoder.simpletron.compiler.translator.utils

import com.logickoder.simpletron.compiler.translator.syntax.Statement
import com.logickoder.simpletron.compiler.translator.syntax.SyntaxError

/**
 * Converts a string to a syntax error with a simple string appended to it
 * */
fun String.syntaxError(index: Int): SyntaxError {
    return SyntaxError("Syntax Error on line ${index + 1}, ${this.trim()}")
}

/**
 * Checks that variables written for the input and print statements in the program are topnotch
 * */
fun Statement.validateVariables() {
    // make sure that a single letter variable is used
    require(action.length == 1 && action[0].isLetter()) {
        "required a letter found \"$action\""
    }
}