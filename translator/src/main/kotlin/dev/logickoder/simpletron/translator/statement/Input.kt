package dev.logickoder.simpletron.translator.statement

import java.util.regex.Pattern

/**
 * Prompt the user to enter an integer.
 * Read that integer from the keyboard and store the integer in the specified variable.
 *
 * Example: 30 input x
 *
 * @property variables the variables to store the input received from the user
 * */
abstract class Input(override val lineNumber: Int, final override val action: String) : Statement {
    protected val variables: List<String>

    init {
        pattern.matcher(action).also { matcher -> require(matcher.matches()) }
        variables = action.split(Regex("\\s*,")).map { it.trim() }
    }

    companion object {
        private const val variable = "[a-z]+"
        private val pattern: Pattern = Pattern.compile("$variable(\\s*,\\s*$variable)*")
    }
}