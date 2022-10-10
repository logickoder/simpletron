package dev.logickoder.simpletron.translator.statement

/**
 * Terminates program execution.
 *
 * Example: 99 end
 * */
abstract class End(override val lineNumber: Int, final override val action: String = "") : Statement