package dev.logickoder.simpletron.translator

import dev.logickoder.simpletron.translator.statement.Statement
import java.util.regex.Pattern

/**
 * Defines a subroutine in the program, which is similar to a function in other programming languages
 */
class Subroutine(val name: String, val statements: List<Statement>) {
    override fun equals(other: Any?) = other is Subroutine && other.name == name
    override fun hashCode() = name.hashCode()

    companion object {
        private val PATTERN: Pattern = Pattern.compile("([a-z]+):")
        const val DEFAULT = "main"

        val String.subroutine: String?
            get() = Subroutine.PATTERN.matcher(this).run {
                if (matches()) {
                    group(1)
                } else null
            }
    }
}