package dev.logickoder.simpletron.translator.compiler

/**
 * An error that occurs during compilation
 */
class CompileError(message: String) : RuntimeException(message)