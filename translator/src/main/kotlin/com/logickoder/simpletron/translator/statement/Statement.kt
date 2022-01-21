package com.logickoder.simpletron.translator.statement

import com.logickoder.simpletron.translator.syntax.SyntaxElement

/**
 *
 */
/**
 *
 * Describes a word that performs an action and can be translated to machine code
 *
 * @property action contains the code to be executed by this keyword
 * @property keyword the name of this statement, which is the same as the lowercase name of the class
 * @property lineNumber the line number of this statement in the source code
 */
abstract class Statement(
    val lineNumber: Int,
    val action: String
) : SyntaxElement {

    val keyword: String = this::class.simpleName?.lowercase() ?: ""

    init {
        require(keyword.isNotBlank()) {
            "A keyword's name cannot be empty\n" +
                    "This might be as a result of creating an anonymous keyword class"
        }
    }
}