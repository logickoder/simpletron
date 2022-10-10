package dev.logickoder.simpletron.translator.statement

import java.util.regex.Pattern

/**
 * Compare two values for equality and transfer program control to
 * the specified line if the condition is true; otherwise,
 * continue execution with the next statement.
 *
 * Example: 35 if i == z goto 80
 *
 * @property symbols the alphanumeric values used in the expression
 * @property operator the logical operator used in the expression
 * @property gotoLocation the location to goto if the expression evaluates to true
 * */
abstract class If(override val lineNumber: Int, final override val action: String) : Statement {
    protected val symbols: List<String>
    protected val operator: String
    protected val gotoLocation: String

    init {
        val ifMatcher = ifPattern.matcher(action).also { matcher ->
            require(matcher.find(0))
            symbols = listOf(matcher.group(1), matcher.group(3))
            operator = matcher.group(2)
        }
        gotoPattern.matcher(action.substring(ifMatcher.end())).also { matcher ->
            require(matcher.find()) { "goto statement not found" }
            gotoLocation = matcher.group(1)
        }
    }

    companion object {
        private const val symbol = "([a-z]+|[-]?[0-9]+)"
        private val ifPattern = Pattern.compile("$symbol\\s*(==|<=|>=|<|>)\\s*$symbol")
        private val gotoPattern = Pattern.compile("\\s+goto\\s+(\\d+)")
    }
}