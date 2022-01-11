package com.logickoder.simpletron.compiler.translator.syntax

/**
 *
 * A message containing what is wrong with a line of code in simpletron basic language
 *
 * @property message what this error is all about
 */
data class SyntaxError(
    val message: String
)
