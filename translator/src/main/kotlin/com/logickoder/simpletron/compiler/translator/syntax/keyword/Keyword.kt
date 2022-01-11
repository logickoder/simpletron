package com.logickoder.simpletron.compiler.translator.syntax.keyword

/**
 *
 * Describes a word that performs an action and can be translated to machine code
 *
 * @property action contains the code to be executed by this keyword
 * @property dependsOn a keyword this keyword depends on to function well
 * @property keyword the name of this keyword, which is the same as the lowercase name of this class
 * @property lineNumber the line number of this keyword in the source code
 */
abstract class Keyword(
    val lineNumber: Int,
    val action: String,
) {
    open var dependsOn: Keyword? = null
    val keyword: String = this::class.simpleName?.lowercase() ?: ""

    init {
        require(keyword.isNotBlank()) {
            "A keyword's name cannot be empty\n" +
                    "This might be as a result of creating an anonymous keyword class"
        }
    }
}