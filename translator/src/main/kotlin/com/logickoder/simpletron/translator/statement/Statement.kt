package com.logickoder.simpletron.translator.statement

/**
 *
 * Describes a word that performs an action and can be translated to machine code
 *
 * @property action contains the code to be executed by this keyword
 * @property keyword the name of this statement
 * @property lineNumber the line number of this statement in the source code
 */
abstract class Statement(
    val lineNumber: Int,
    val action: String,
    val keyword: String
) {

    init {
        require(keyword.isNotBlank()) {
            "A keyword's name cannot be empty\n" +
                    "This might be as a result of creating an anonymous keyword class"
        }
    }
}