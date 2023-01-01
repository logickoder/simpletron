package dev.logickoder.simpletron.translator.statement

import java.util.regex.Pattern

/**
 * Display the value of the specified variable on screen
 *
 * Example: 10 print w
 *
 * @property symbols the alphanumeric values to print on the screen
 * */
abstract class Print(override val lineNumber: Int, final override val action: String) : Statement {
    protected val symbols: List<String>

    init {
        pattern.matcher(action).also { matcher -> require(matcher.matches()) }
        symbols = action.split(Regex("\\s*,")).map { it.trim() }
    }

    companion object {
        private const val variable = "([a-z]+|[0-9]+)"
        val pattern: Pattern = Pattern.compile("$variable(\\s*,\\s*$variable)*")
    }
}