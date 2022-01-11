package com.jeffreyorazulike.simpletron.compiler.translator.syntax.keywords

import com.jeffreyorazulike.simpletron.compiler.translator.syntax.keyword.Keyword

/**
 * Any text following the command rem is for documentation
 * purposes only and is ignored by the translator.
 *
 * Example: 50 rem this is a remark
 * */
class Rem(lineNumber: Int, action: String) : Keyword(lineNumber, action)

/**
 * Display a question mark to prompt the user to enter an integer.
 * Read that integer from the keyboard and store the integer in the specified variable.
 *
 * Example: 30 input x
 * */
class Input(lineNumber: Int, action: String) : Keyword(lineNumber, action)

/**
 * Assign the variable on the left the value on the right.
 * Note that an arbitrarily complex expression can appear to
 * the right of the equal sign.
 *
 * Example: 80 let u = 4 * (j - 56)
 * */
class Let(lineNumber: Int, action: String) : Keyword(lineNumber, action)

/**
 * Display the value of the specified variable on screen
 *
 * Example: 10 print w
 * */
class Print(lineNumber: Int, action: String) : Keyword(lineNumber, action)

/**
 * Compare two values for equality and transfer program control to
 * the specified line if the condition is true; otherwise,
 * continue execution with the next statement.
 *
 * Example: 35 if i == z goto 80
 * */
class If(lineNumber: Int, action: String) : Keyword(lineNumber, action) {
}

/**
 * Transfer program control to the specified line.
 *
 * Example: 70 goto 45
 * */
class Goto(lineNumber: Int, action: String) : Keyword(lineNumber, action) {
}

/**
 * Terminates program execution.
 *
 * Example: 99 end
 * */
class End(lineNumber: Int, action: String = "") : Keyword(lineNumber, action)