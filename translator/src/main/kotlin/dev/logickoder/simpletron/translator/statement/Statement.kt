package dev.logickoder.simpletron.translator.statement

import dev.logickoder.simpletron.translator.config.Config

/**
 *
 * Describes a word that performs an action and can be translated to machine code
 *
 * @property lineNumber the line number of this statement in the source code
 * @property action contains the code to be executed by this keyword
 */
interface Statement {
    val lineNumber: Int
    val action: String

    fun <T : Config> translate(config: T): List<Int>
}