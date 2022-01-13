package com.logickoder.simpletron.compiler.translator.utils

import com.logickoder.simpletron.compiler.translator.Translator
import com.logickoder.simpletron.compiler.translator.syntax.SyntaxElement
import com.logickoder.simpletron.compiler.translator.syntax.SyntaxError
import java.util.regex.Pattern

fun Translator.extractStatement(line: String, index: Int): SyntaxElement {
    // parse the line to make sure it looks like a correct statement
    val matcher = Pattern.compile("(\\d+)\\s+([a-z]+)(\\s+[!-~\\s+]+)?").matcher(line.lowercase())

    return matcher.run {
        if (matches()) {
            // check if the statement exists, if it doesn't throw a syntax error
            val statement = statements.find { it.statementName == group(2) }
                ?: return "\"${group(2)}\" is not a valid keyword".syntaxError(index)
            try {
                // create an instance of the statement with the default arguments
                statement.create(group(1).toInt(), group(3)?.trim() ?: "")
            } catch (exception: IllegalArgumentException) {
                (exception.message ?: SyntaxError.UNKNOWN_ERROR).syntaxError(index)
            }
        } else {
            // throw a syntax error if the line wasn't constructed correctly
            "unable to parse \"$line\"".syntaxError(index)
        }
    }
}

fun Translator.extractStatement(index: Int) = extractStatement(lines[index], index)