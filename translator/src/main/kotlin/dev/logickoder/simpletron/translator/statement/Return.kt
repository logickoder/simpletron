package dev.logickoder.simpletron.translator.statement

/**
 * Ends a subroutine and returns the program back to where the subroutine was called from
 *
 * Example: 80 return
 */
abstract class Return(override val lineNumber: Int, final override val action: String = "") : Statement