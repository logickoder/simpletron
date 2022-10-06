package com.logickoder.simpletron.translator.statement

/**
 *
 * Describes a word that performs an action and can be translated to machine code
 *
 * @property lineNumber the line number of this statement in the source code
 * @property action contains the code to be executed by this keyword
 */
abstract class Statement(
    val lineNumber: Int,
    val action: String
)