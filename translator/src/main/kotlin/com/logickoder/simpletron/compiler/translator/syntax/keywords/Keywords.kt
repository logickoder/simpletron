package com.logickoder.simpletron.compiler.translator.syntax.keywords

import com.logickoder.simpletron.compiler.translator.syntax.keyword.Keyword
import com.logickoder.simpletron.compiler.translator.utils.validateVariables
import java.util.regex.Pattern

/**
 * Any text following the command rem is for documentation
 * purposes only and is ignored by the translator.
 *
 * Example: 50 rem this is a remark
 * */
class Rem(lineNumber: Int, comment: String) : Keyword(lineNumber, comment) {
    init {
        require(comment.isNotBlank()) {
            "a comment must come after a rem statement"
        }
    }
}

/**
 * Prompt the user to enter an integer.
 * Read that integer from the keyboard and store the integer in the specified variable.
 *
 * Example: 30 input x
 * */
class Input(lineNumber: Int, variable: String) : Keyword(lineNumber, variable) {
    init {
        validateVariables()
    }
}

/**
 * Assign the variable on the left the value on the right.
 * Note that an arbitrarily complex expression can appear to
 * the right of the equal sign.
 *
 * Example: 80 let u = 4 * (j - 56)
 * */
class Let(lineNumber: Int, equation: String) : Keyword(lineNumber, equation) {
    init {
        val matcher = Pattern
            .compile("([a-z])\\s*=([+\\-*/^%\\w]+)", Pattern.CASE_INSENSITIVE)
            .matcher(equation)

        require(matcher.matches()) {
            "malformed equation"
        }
    }
}

/**
 * Display the value of the specified variable on screen
 *
 * Example: 10 print w
 * */
class Print(lineNumber: Int, action: String) : Keyword(lineNumber, action) {
    init {
        validateVariables()
    }
}

/**
 * Compare two values for equality and transfer program control to
 * the specified line if the condition is true; otherwise,
 * continue execution with the next statement.
 *
 * Example: 35 if i == z goto 80
 * */
class If(lineNumber: Int, expression: String) : Keyword(lineNumber, expression) {
    init {
        val variableRegex = Regex("[a-z]|[0-9]+")
        val matcher = Pattern
            .compile(
                "(${variableRegex.pattern})\\s*(==|<=|>=|<|>)\\s*(${variableRegex.pattern})\\s+goto\\s+(\\d+)",
                Pattern.CASE_INSENSITIVE
            ).matcher(expression);
        require(matcher.matches()) {
            "invalid boolean expression"
        }
    }
}

/**
 * Transfer program control to the specified line.
 *
 * Example: 70 goto 45
 * */
class Goto(lineNumber: Int, line: String) : Keyword(lineNumber, line) {
    init {
        require(line.toIntOrNull() != null) {
            "required line number, found \"$line\""
        }
    }
}

/**
 * Terminates program execution.
 *
 * Example: 99 end
 * */
class End(lineNumber: Int, action: String = "") : Keyword(lineNumber, action)