package com.logickoder.simpletron.translator

import com.logickoder.simpletron.core.Simpletron
import com.logickoder.simpletron.translator.statement.Statement
import com.logickoder.simpletron.translator.statement.statements.StatementFactory
import com.logickoder.simpletron.translator.syntax.SyntaxError
import com.logickoder.simpletron.translator.syntax.syntaxError
import java.util.regex.Pattern

/**
 *
 * Translator is the base class that defines programs to handle the conversion of Simpletron basic language to its
 * machine counterpart
 *
 * @property statements the list of statements this translator can parse
 */
abstract class Translator(
    val simpletron: Simpletron
) {
    abstract val statements: List<StatementFactory<Statement>>

    /**
     * Converts a simpletron basic language program line to a statement or
     * throws a syntax error if a problem arises while parsing the lines
     */
    open fun extractStatement(index: Int, line: String): Statement {
        // parse the line to make sure it looks like a correct statement
        return PATTERN.matcher(line.lowercase()).run {
            if (matches()) {
                val lineNumber = group(1).toInt()
                val keyword = group(2)
                val expression = group(3)?.trim() ?: ""
                // check if the statement exists, if it doesn't throw a syntax error
                val factory = statements.find { it.keyword == keyword }
                    ?: throw "\"$keyword\" is not a valid keyword".syntaxError(index)
                try {
                    // uses the statement factory to create a statement with the corresponding keyword
                    factory.create(lineNumber, expression)
                } catch (exception: IllegalArgumentException) {
                    // an exception is thrown if there was a problem while parsing the statement
                    throw (exception.message ?: SyntaxError.UNKNOWN_ERROR).syntaxError(index)
                }
            } else {
                // throw a syntax error if the line wasn't constructed correctly
                throw "unable to parse \"$index\"".syntaxError(index)
            }
        }
    }

    /**
     * Executes the program in the file specified
     *
     * @param pathToFile path to the file
     */
    abstract fun run(pathToFile: String)

    companion object {
        private val PATTERN: Pattern = Pattern.compile("(\\d+)\\s+([a-z]+)(\\s+[!-~\\s+]+)?")
    }
}