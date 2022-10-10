package dev.logickoder.simpletron.translator.statement

import java.util.regex.Pattern

/**
 * Calls a subroutine, which is similar to a function call in most programming languages
 *
 * Example: 20 gosub repeat
 *
 * @param action the name of the subroutine
 */
abstract class Gosub(override val lineNumber: Int, final override val action: String) : Statement {
    protected val name: String = pattern.matcher(action).let { matcher ->
        require(matcher.matches()) { "invalid name for a subroutine" }
        matcher.group(0)
    }

    companion object {
        private val pattern = Pattern.compile("[a-z]+")
    }
}