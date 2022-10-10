package dev.logickoder.simpletron.translator.statement

/**
 * Any text following the command rem is for documentation
 * purposes only and is ignored by the translator.
 *
 * Example: 50 rem this is a remark
 * */
abstract class Rem(override val lineNumber: Int, final override val action: String) : Statement