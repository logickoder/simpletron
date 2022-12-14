package dev.logickoder.simpletron.translator.syntax

/**
 *
 * A message containing what is wrong with a line of code in simpletron basic language
 *
 * @property message what this error is all about
 */
class SyntaxError(message: String) : RuntimeException(message) {
    companion object {
        const val UNKNOWN_ERROR = "unknown error occurred"
    }
}