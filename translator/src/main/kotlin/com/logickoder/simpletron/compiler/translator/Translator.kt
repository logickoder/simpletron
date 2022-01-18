package com.logickoder.simpletron.compiler.translator

import com.logickoder.simpletron.translator.core.syntax.SyntaxElement
import com.logickoder.simpletron.translator.core.syntax.SyntaxError
import com.logickoder.simpletron.translator.core.syntax.syntaxError
import com.logickoder.simpletron.translator.statement.statements.statementFactories
import java.util.regex.Pattern

/**
 *
 * Translator is the base class that defines programs to handle the conversion of Simpletron basic language to its
 * machine counterpart
 *
 * @property statements the list of statements this translator can parse
 * @property lines the lines of a program this translator is parsing
 */
abstract class Translator {
    open val statements by lazy { statementFactories() }
    abstract val lines: List<String>

    open fun extractStatement(line: String, index: Int): SyntaxElement {
        // parse the line to make sure it looks like a correct statement
        val matcher = Pattern.compile("(\\d+)\\s+([a-z]+)(\\s+[!-~\\s+]+)?").matcher(line.lowercase())

        return matcher.run {
            if (matches()) {
                // check if the statement exists, if it doesn't throw a syntax error
                val statement = statements.find { it.statementName == group(2) }
                    ?: return "\"${group(2)}\" is not a valid keyword".syntaxError(index)
                try {
                    // register the line number for the statement
                    // create an instance of the statement with the default arguments
                    statement.create(group(3)?.trim() ?: "")
                } catch (exception: IllegalArgumentException) {
                    (exception.message ?: SyntaxError.UNKNOWN_ERROR).syntaxError(index)
                }
            } else {
                // throw a syntax error if the line wasn't constructed correctly
                "unable to parse \"$line\"".syntaxError(index)
            }
        }
    }
}