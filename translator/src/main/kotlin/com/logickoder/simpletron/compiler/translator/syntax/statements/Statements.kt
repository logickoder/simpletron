package com.logickoder.simpletron.compiler.translator.syntax.statements

import com.logickoder.simpletron.compiler.infix_postfix.evaluateEquation
import com.logickoder.simpletron.compiler.translator.syntax.Statement
import java.util.regex.Pattern

/**
 * Any text following the command rem is for documentation
 * purposes only and is ignored by the translator.
 *
 * Example: 50 rem this is a remark
 * */
class Rem(lineNumber: Int, comment: String) : Statement(lineNumber, comment) {
    init {
        require(comment.isNotBlank()) { "a comment must come after a $keyword statement" }
    }
}

/**
 * Prompt the user to enter an integer.
 * Read that integer from the keyboard and store the integer in the specified variable.
 *
 * Example: 30 input x
 * */
class Input(lineNumber: Int, variable: String) : Statement(lineNumber, variable) {
    init {
        pattern.matcher(action).also { matcher ->
            require(matcher.matches()) { "malformed $keyword statement." }
        }
    }

    companion object {
        private const val variable = "[a-z]+"
        val pattern: Pattern = Pattern.compile("$variable(\\s*[,]\\s*$variable)*")
    }
}

/**
 * Assign the variable on the left the value on the right.
 * Note that an arbitrarily complex expression can appear to
 * the right of the equal sign.
 *
 * Example: 80 let u = 4 * (j - 56)
 * */
class Let(lineNumber: Int, equation: String) : Statement(lineNumber, equation.lowercase()) {
    init {
        require(action.count { it == '=' } == 1) {
            "there must be only one \"=\" in your equation"
        }
        val sides = action.split("=").toMutableList().apply {
            removeIf { it.isBlank() }
            require(size == 2) { "the equation was not written properly" }
        }
        val postfix = sides[1].evaluateEquation()
        println(postfix)
    }
}

/**
 * Display the value of the specified variable on screen
 *
 * Example: 10 print w
 * */
class Print(lineNumber: Int, action: String) : Statement(lineNumber, action) {
    init {
        pattern.matcher(action).also { matcher ->
            require(matcher.matches()) { "malformed $keyword statement." }
        }
    }

    companion object {
        private const val variable = "([a-z]+|[0-9]+)"
        val pattern: Pattern = Pattern.compile("$variable(\\s*[,]\\s*$variable)*")
    }
}

/**
 * Compare two values for equality and transfer program control to
 * the specified line if the condition is true; otherwise,
 * continue execution with the next statement.
 *
 * Example: 35 if i == z goto 80
 * */
class If(lineNumber: Int, expression: String) : Statement(lineNumber, expression.lowercase()) {
    init {
        val ifMatcher = ifPattern.matcher(action).also { matcher ->
            require(matcher.find(0)) { "invalid $keyword expression" }
        }
        gotoPattern.matcher(action.substring(ifMatcher.end())).also { matcher ->
            require(matcher.find()) { "goto statement not found" }
            dependsOn = Goto(lineNumber, matcher.group(1))
        }
    }

    companion object {
        private const val variable = "([a-z]+|[0-9]+)"
        private val ifPattern = Pattern.compile("$variable\\s*(==|<=|>=|<|>)\\s*$variable")
        private val gotoPattern = Pattern.compile("\\s+goto\\s+(\\d+)")
    }
}

/**
 * Transfer program control to the specified line.
 *
 * Example: 70 goto 45
 * */
class Goto(lineNumber: Int, line: String) : Statement(lineNumber, line.lowercase()) {
    init {
        require(action.toIntOrNull() != null) { "required line number, found \"$line\"" }
    }
}

/**
 * Terminates program execution.
 *
 * Example: 99 end
 * */
class End(lineNumber: Int, action: String = "") : Statement(lineNumber, action)