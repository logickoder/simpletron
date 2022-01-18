package com.logickoder.simpletron.translator.statement

import com.logickoder.simpletron.translator.core.syntax.SyntaxElement


/**
 *
 * Describes a word that performs an action and can be translated to machine code
 *
 * @property action contains the code to be executed by this keyword
 * @property dependsOn a statement this statement depends on to function well
 * @property keyword the name of this statement, which is the same as the lowercase name of the class
 * @property lineNumber the line number of this statement in the source code
 */
abstract class Statement(val action: String) : SyntaxElement {

    val keyword: String = this::class.simpleName?.lowercase() ?: ""

    init {
        require(keyword.isNotBlank()) {
            "A keyword's name cannot be empty\n" +
                    "This might be as a result of creating an anonymous keyword class"
        }
    }

    /**
     * Translates this statement to machine code which may span lines
     *
     * @param config the configuration used by this statement during translation
     */
    abstract fun translate(config: StatementConfig): MachineCode
}