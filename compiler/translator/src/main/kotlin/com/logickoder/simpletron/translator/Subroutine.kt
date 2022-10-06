package com.logickoder.simpletron.translator

import com.logickoder.simpletron.translator.statement.Statement
import java.util.regex.Pattern

/**
 * Defines a subroutine in the program, which is similar to a function in other programming languages
 */
class Subroutine(val name: String, val statements: List<Statement>) {
    override fun equals(other: Any?) = other is Subroutine && other.name == name
    override fun hashCode() = name.hashCode()

    companion object {
        val PATTERN: Pattern = Pattern.compile("([a-z]+):")
        const val DEFAULT = "main"

        fun isSubroutine(line: String) = PATTERN.matcher(line).matches()
    }
}